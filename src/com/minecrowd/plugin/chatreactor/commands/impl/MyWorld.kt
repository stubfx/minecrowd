package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType
import java.util.*


object MyWorld : Command() {

    override val defaultCoolDown: Long = 1000 * 1000
    override val cost: Int = 1000

    override fun behavior(playerName: String, options: String?) {
//        var allInFlatland = false
//        while (!allInFlatland) {
//            // respawn all the player just to make sure.
////            CommandRunner.forEachPlayer {
////                // Note that this will not work on CraftBukkit servers ??????
////                it.spigot().respawn()
////            }
//            // force them in the flatland
//            FlatLand.forceRun(isSilent = true)
//            Thread.sleep(1000)
//            // if they are actually all there, we good.
//            CommandRunner.forEachPlayer {
//                allInFlatland = allInFlatland && (it.world.name == FLATLAND_NAME)
//            }
//            PluginUtils.log("ALLINFLATLAND $allInFlatland")
//        }
//        // let's start to remove the basic world first.
//        val prevWorld = Bukkit.getWorld("world")
//        prevWorld!!.keepSpawnInMemory = false // to unload the world completly
//
//        Bukkit.unloadWorld(prevWorld, false)
//
//        val src = File(Bukkit.getWorldContainer().toString() + File.separator + prevWorld.name)
//        FileUtils.deleteDirectory(src)
//        // then create the new one.
        val wc = WorldCreator("world_$playerName")

        wc.environment(World.Environment.NORMAL)
        wc.type(WorldType.NORMAL)
        wc.seed(Date().time)

        val world = wc.createWorld()
        CommandRunner.forEachPlayer {
            if (world?.spawnLocation != null) {
                if (it.teleport(world.spawnLocation)) {
                    it.bedSpawnLocation = it.location
                }
            }
        }
    }

}