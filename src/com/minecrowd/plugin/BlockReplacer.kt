package com.minecrowd.plugin

import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.scheduler.BukkitRunnable

/**
 * The BlockReplacer class provides methods for replacing blocks in a specific area.
 */
object BlockReplacer {

    private const val CHUNK_BLOCKS_NUMBER = 15.0
    private const val SPAWN_SAFE_RADIUS = 15.0
    lateinit var main: Main
    const val threadBlockY = 10

    /**
     * Check if a block is close to the spawn area based on its location.
     *
     * @param block the block to check
     * @return true if the block is close to the spawn area, false otherwise
     */
    fun isBlockCloseToSpawnArea(block: Block): Boolean {
        val spawnLocation = block.world.spawnLocation
        val blockLocation = block.location
        return blockLocation.x > spawnLocation.x - SPAWN_SAFE_RADIUS &&
                blockLocation.x < spawnLocation.x + SPAWN_SAFE_RADIUS &&
                blockLocation.z > spawnLocation.z - SPAWN_SAFE_RADIUS &&
                blockLocation.z < spawnLocation.z + SPAWN_SAFE_RADIUS
    }

    /**
     * Sets the main reference for the software.
     *
     * @param mainRef The main reference to be set.
     */
    fun setMainRef(mainRef: Main) {
        main = mainRef
    }

    /**
     * Iterates over each block within a specified region defined by two locations (inclusive) and performs the specified action.
     * This method is deprecated. Use [forEachBlockAsync] instead.
     *
     * @param loc1 the first location defining the region
     * @param loc2 the second location defining the region
     * @param func the action to be performed on each block
     * @deprecated Use [forEachBlockAsync] instead.
     */
    @Deprecated("Use forEachBlockAsync instead.")
    fun forEachBlock(loc1: Location, loc2: Location, func: (Block) -> Unit) {
        PluginUtils.checkLocationsWorld(loc1, loc2)
        val minL = PluginUtils.getMinLocation(loc1, loc2)
        val maxL = PluginUtils.getMaxLocation(loc1, loc2)
        for (y in minL.y.toInt()..maxL.y.toInt()) {
            for (x in minL.x.toInt()..maxL.x.toInt()) {
                for (z in minL.z.toInt()..maxL.z.toInt()) {
                    func(Location(loc1.world, x.toDouble(), y.toDouble(), z.toDouble()).block)
                }
            }
        }
    }

    /**
     * Asynchronously iterates over each block within the specified range of locations and performs the given action on each block.
     *
     * @param loc1 The first location defining the range.
     * @param loc2 The second location defining the range.
     * @param func The function to be executed on each block.
     * @param onFinish The function to be executed when the iteration is complete. Default is an empty function.
     */
    fun forEachBlockAsync(loc1: Location, loc2: Location, func: (Block) -> Unit, onFinish: () -> Unit = {}) {
        PluginUtils.checkLocationsWorld(loc1, loc2)
        val minL = PluginUtils.getMinLocation(loc1, loc2)
        val maxL = PluginUtils.getMaxLocation(loc1, loc2)
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
                            // check for spawn!!
                            val block = Location(loc1.world, x.toDouble(), y.toDouble(), z.toDouble()).block
                            if (!isBlockCloseToSpawnArea(block)) {
                                func(block)
                            }
                        }
                    }
                }
                // increase the offset
                zero += threadBlockY
            }
        }.runTaskTimer(main, 1, 2)
    }

    /**
     * Replaces all blocks within a given chunk with a specified material.
     *
     * @param chunk the chunk to replace blocks in
     * @param material the material to replace the blocks with
     * @param excluded an optional list of materials to exclude from replacement
     * @param onFinish a callback function to be called when the replacement is finished
     */
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
        replaceAreaAsync(
            start,
            end,
            material,
            excluded,
            excludeAir = true,
            onFinish = onFinish
        )
    }

    /**
     * Replaces the blocks within a specified area with a new material.
     *
     * @param loc1 The first location defining the area.
     * @param loc2 The second location defining the area.
     * @param material The material to replace the blocks with.
     * @param excluded A list of materials that should be excluded from the replacement. Defaults to null.
     * @param excludeAir Determines whether air blocks should be excluded from the replacement. Defaults to true.
     *
     * @deprecated Use [replaceAreaAsync] instead.
     */
    @Deprecated("Use replaceAreaAsync instead.")
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
                @Suppress("DEPRECATION")
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

    /**
     * Replaces blocks within a specified area with a new material asynchronously.
     *
     * @param loc1 The first location defining the area.
     * @param loc2 The second location defining the area.
     * @param material The material to replace the blocks with.
     * @param excluded A list of materials to exclude from replacement. Defaults to null.
     * @param onlyIfIsTypeOf A list of materials to only replace if the block type is in the list.
     * @param excludeAir Flag indicating whether to exclude air blocks from replacement. Defaults to true.
     * @param onFinish Callback function to be executed when replacement is finished. Defaults to an empty function.
     */
    fun replaceAreaAsync(
        loc1: Location,
        loc2: Location,
        material: Material,
        excluded: List<Material>? = null,
        onlyIfIsTypeOf: List<Material> = mutableListOf(),
        excludeAir: Boolean = true,
        onFinish: () -> Unit = {}
    ) {
        PluginUtils.checkLocationsWorld(loc1, loc2)
        forEachBlockAsync(loc1, loc2, { block ->
            if (onlyIfIsTypeOf.isNotEmpty()) {
                // in this case we must replace only if the material is in the list.
                if (!onlyIfIsTypeOf.contains(block.type)) return@forEachBlockAsync
            }
            if (excludeAir && block.type.isAir) return@forEachBlockAsync
            if (excluded?.contains(block.type) != true) {
                // well, that's not even in the excluded list...
                if (!isBlockCloseToSpawnArea(block)) {
                    // what a poor block.
                    block.type = material
                }
            }
        }, onFinish)
    }

}