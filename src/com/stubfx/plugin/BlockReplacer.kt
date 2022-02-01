package com.stubfx.plugin

import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.scheduler.BukkitRunnable

object BlockReplacer {

    const val CHUNK_BLOCKS_NUMBER = 15.0
    lateinit var main: Main
    const val threadBlockY = 10

    fun setMainRef(mainRef: Main) {
        main = mainRef
    }

    fun forEachBlock(loc1: Location, loc2: Location, func: (Block) -> Unit) {
        PluginUtils.checkLocationsWorld(loc1, loc2)
        val minL = PluginUtils.getMinLocation(loc1, loc2)
        val maxL = PluginUtils.getMaxLocation(loc1, loc2)
        main.server.consoleSender.sendMessage(minL.toString())
        main.server.consoleSender.sendMessage(maxL.toString())
        for (y in minL.y.toInt()..maxL.y.toInt()) {
            for (x in minL.x.toInt()..maxL.x.toInt()) {
                for (z in minL.z.toInt()..maxL.z.toInt()) {
                    func(Location(loc1.world, x.toDouble(), y.toDouble(), z.toDouble()).block)
                }
            }
        }
    }

    fun forEachBlockAsync(loc1: Location, loc2: Location, func: (Block) -> Unit, onFinish: () -> Unit = {}) {
        PluginUtils.checkLocationsWorld(loc1, loc2)
        val minL = PluginUtils.getMinLocation(loc1, loc2)
        val maxL = PluginUtils.getMaxLocation(loc1, loc2)
        main.server.consoleSender.sendMessage(minL.toString())
        main.server.consoleSender.sendMessage(maxL.toString())
        // let the y be first, so we know that the number of blocks (and threads)
        // that can be modified is limited.
        var zero = minL.y.toInt()
        object : BukkitRunnable() {
            override fun run() {
                if (zero > maxL.y.toInt()) {
                    cancel()
                    onFinish()
                    return
                }
                // otherwise, just run it.
                val maxY = zero + threadBlockY
                for (y in zero..maxY) {
                    // in some cases the offset may go over the max y.
                    if (y > maxL.y.toInt()) break
                    ////////////////////////////
                    for (x in minL.x.toInt()..maxL.x.toInt()) {
                        for (z in minL.z.toInt()..maxL.z.toInt()) {
                            func(Location(loc1.world, x.toDouble(), y.toDouble(), z.toDouble()).block)
                        }
                    }
                }
                // increase the offset
                zero += threadBlockY
            }
        }.runTaskTimer(main, 1, 2)
    }

    fun chunkReplace(chunk: Chunk, material: Material, excluded: List<Material>? = null, onFinish: () -> Unit = {}) {
        // get location of the chunk
        // keep in mind that chunk.x gets the chunk index, not the world index,
        // so we need to extract the coordinates of the first block.
        val block: Block = chunk.getBlock(0, 0, 0)
        val start = Location(block.world, block.x.toDouble(), -63.0, block.z.toDouble())
        val end =
            Location(
                block.world,
                block.x.toDouble() + CHUNK_BLOCKS_NUMBER,
                255.0,
                block.z.toDouble() + CHUNK_BLOCKS_NUMBER
            )
        main.server.consoleSender.sendMessage(start.toString())
        main.server.consoleSender.sendMessage(end.toString())
        main.server.consoleSender.sendMessage(material.toString())
        replaceAreaAsync(start, end, material, excluded, true, onFinish)
    }

    fun replaceArea(
        loc1: Location,
        loc2: Location,
        material: Material,
        excluded: List<Material>? = null,
        excludeAir: Boolean = true
    ) {
        object : BukkitRunnable() {
            override fun run() {
                PluginUtils.checkLocationsWorld(loc1, loc2)
                forEachBlock(loc1, loc2) { block ->
                    if (excludeAir && block.type.isAir) return@forEachBlock
                    if (excluded?.contains(block.type) != true) {
                        // well, that's not even in the excluded list...
                        // what a poor block.
                        block.type = material
                    }
                }

            }
        }.runTask(main)
    }

    fun replaceAreaAsync(
        loc1: Location,
        loc2: Location,
        material: Material,
        excluded: List<Material>? = null,
        excludeAir: Boolean = true,
        onFinish: () -> Unit = {}
    ) {
        PluginUtils.checkLocationsWorld(loc1, loc2)
        forEachBlockAsync(loc1, loc2, { block ->
            if (excludeAir && block.type.isAir) return@forEachBlockAsync
            if (excluded?.contains(block.type) != true) {
                // well, that's not even in the excluded list...
                // what a poor block.
                block.type = material
            }
        }, onFinish)
    }

}