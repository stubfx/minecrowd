package com.stubfx.plugin

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class MainKt : JavaPlugin() {

    private var player: Player? = null
    private var myTask: BukkitTask? = null

    override fun onEnable() {
        getCommand("clearchunk")?.tabCompleter = MyMaterialTabCompleter()
    }

    fun removeBlock(block: Block?) {
        block?.type = Material.AIR
    }

    override fun onDisable() {
        clearTask()
    }


    private fun clearTask() {
        myTask?.cancel()
    }


    @Override
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        clearTask()
        player = sender as Player
        if (player == null) {
            return false
        }
        // in this case we found me.
        checkCommand(command, args)
        return false
    }

    private fun checkCommand(command: Command, args: Array<String>) {
        when (command.name.lowercase()) {
            "draw" -> draw()
            "jesus" -> jesus()
            "drill" -> drill()
            "clearchunk" -> clearchunk(args)
            "stopcommand" -> clearTask()
        }
    }

    private fun getPlayerLocation(): Location? {
        return player?.location
    }

    private fun clearchunk(args: Array<String>) {
        val location = getPlayerLocation()!!
        val playerHeight = location.y
        val chunk = location.chunk
        myTask = object : BukkitRunnable() {
            override fun run() {
                val materialsToExclude = mutableListOf<Material>()
                for (arg in args) {
                    materialsToExclude.add(Material.valueOf(arg))
                }
                if (player != null) {
                    for (x in 0..15) {
                        for (z in 0..15) {
                            for (y in playerHeight.toInt() downTo 1) {
                                // this runs for any selected block of the chunk
                                // we need to convert to air, only if they are not ores
                                val block = chunk.getBlock(x, y, z)
                                // check if block type is not contained in the 'exclude list'
                                if (!materialsToExclude.contains(block.type)) {
                                    // here if the block is not what we are looking for
                                    // so we convert that into air
                                    block.type = Material.AIR
                                }
                            }
                        }
                    }
                }
            }
        }.runTask(this)
    }

    private fun drill() {
        myTask = object : BukkitRunnable() {
            override fun run() {
                val location = getPlayerLocation()
                if (player != null) {
                    for (z in 0..3) {
                        for (x in 0..3) {
                            for (y in 0..3) {
                                location?.clone()?.add(x - 1.toDouble(), y.toDouble(), z - 1.toDouble())?.block?.type = Material.AIR
                            }
                        }
                    }
                }
                location?.block?.type = Material.TORCH
            }
        }.runTaskTimer(this, 1, 1)
    }

    private fun jesus() {
        myTask = object : BukkitRunnable() {
            override fun run() {
                if (player != null) {
                    getPlayerLocation()?.add(0.toDouble(), (-1).toDouble(), 0.toDouble())?.block?.type = Material.STONE
                }
            }
        }.runTaskTimer(this, 1, 1)
    }

    private fun draw() {
        // in this case we want to activate the command.
        myTask = object : BukkitRunnable() {
            override fun run() {
                val targetBlockExact = player?.getTargetBlockExact (20)
                targetBlockExact?.type = player?.inventory?.itemInMainHand?.type ?: Material.AIR
            }
        }.runTaskTimer(this, 1, 1)
    }


}