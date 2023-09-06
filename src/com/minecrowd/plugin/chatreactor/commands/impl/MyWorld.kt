package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType


object MyWorld : Command() {

    override val defaultCoolDown: Long = 1000 * 1000
    override val cost: Int = 1000

    override fun behavior(playerName: String, options: String?) {
        val wc = WorldCreator("world_$playerName")

        wc.environment(World.Environment.NORMAL)
        wc.type(WorldType.NORMAL)

        val world = wc.createWorld()
        CommandRunner.forEachPlayer {
            if (world?.spawnLocation != null) {
                it.teleport(world.spawnLocation)
            }
        }
    }

}