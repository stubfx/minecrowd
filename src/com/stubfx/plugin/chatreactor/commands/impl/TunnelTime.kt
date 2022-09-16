package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import org.bukkit.entity.EntityType

object TunnelTime : Command() {


    override fun defaultCoolDown(): Long {
        return 180 * 1000
    }

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