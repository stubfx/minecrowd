package com.stubfx.plugin

import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

object BlockReplacer {

    const val CHUNK_BLOCKS_NUMBER = 15.0

    fun chunkReplace(plugin: JavaPlugin, chunk: Chunk, material: Material, excluded: List<Material>? = null) {
        // get location of the chunk
        // keep in mind that chunk.x gets the chunk index, not the world index
        // so we need to extract the coordinates of the first block.
        val block: Block = chunk.getBlock(0,0,0)
        val start = Location(block.world, block.x.toDouble(), 0.0, block.z.toDouble())
        val end =
            Location(block.world, block.x.toDouble() + CHUNK_BLOCKS_NUMBER, 255.0, block.z.toDouble() + CHUNK_BLOCKS_NUMBER)
        plugin.server.consoleSender.sendMessage(start.toString())
        plugin.server.consoleSender.sendMessage(end.toString())
        plugin.server.consoleSender.sendMessage(material.toString())
        replaceArea(plugin, start, end, material, excluded, true)
    }

    fun replaceAreaExAir(plugin: JavaPlugin, loc1: Location, loc2: Location, material: Material, excluded: List<Material>? = null){
        val materialToExclude: MutableList<Material> = ArrayList()
        replaceArea(plugin, loc1, loc2, material, materialToExclude, true)
    }

    fun replaceArea(plugin: JavaPlugin, loc1: Location, loc2: Location, material: Material, excluded: List<Material>? = null, excludeAir: Boolean = true) {
        object : BukkitRunnable() {
            override fun run() {
                Utils.checkLocationsWorld(loc1, loc2)
                val minL = Utils.getMinLocation(loc1, loc2)
                val maxL = Utils.getMaxLocation(loc1, loc2)
                plugin.server.consoleSender.sendMessage(minL.toString())
                plugin.server.consoleSender.sendMessage(maxL.toString())
                for (x in minL.x.toInt()..maxL.x.toInt()) {
                    for (z in minL.z.toInt()..maxL.z.toInt()) {
                        for (y in minL.y.toInt()..maxL.y.toInt()) {
                            val block = Location(loc1.world, x.toDouble(), y.toDouble(), z.toDouble()).block
                            // if we need to exclude air, and the block is actually air, just skip.
                            if (excludeAir && block.type.isAir) continue
                            // in this case we need to make sure that is not in the excluded list.
                            if (excluded?.contains(block.type) != true) {
                                // well, that's not even in the excluded list...
                                // what a poor block.
                                block.type = material
                            }
                        }
                    }
                }
            }
        }.runTask(plugin)
    }

}