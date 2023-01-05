package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.entity.EntityType

object TunnelTime : Command() {


    override val defaultCoolDown: Long = 180 * 1000
    override val cost: Int = 250

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forRandomPlayer {
            val target = it.getTargetBlockExact(50) ?: return@forRandomPlayer
            val world = it.world
            for (i in 0..50) {
                val location = target.location.add(it.location.direction.multiply(i))
                world.spawnEntity(location, EntityType.PRIMED_TNT)
            }
        }
    }

}