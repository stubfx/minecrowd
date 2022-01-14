package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType

object TunnelTime: Command() {

    override fun commandType(): CommandType = CommandType.TUNNELTIME

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val target = it.getTargetBlockExact(50) ?: return@forEachPlayer
            val world = it.world
            for (i in 0..50) {
                val location = target.location.add(it.location.direction.multiply(i))
                world.spawnEntity(location, EntityType.PRIMED_TNT)
            }
        }
    }

}