package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.entity.EntityType

object MeteorShower : Command() {


    override val defaultCoolDown: Long = 60 * 5 * 1000
    override val cost: Int = 600

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.startRecurrentTask {
            CommandRunner.forRandomPlayer {
                val closeLocation = getCloseLocation(it.location, 50.0)
                it.world.spawnEntity(closeLocation, EntityType.PRIMED_TNT)
            }
        }
    }

}