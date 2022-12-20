package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandResultWrapper
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.entity.EntityType

object WhatIsThat : Command() {


    override fun defaultCoolDown(): Long {
        return 120 * 1000 // 2 mins
    }

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.startShortRecurrentTask {
            CommandRunner.forRandomPlayer {
                val direction = it.location.direction.clone()
                val spawnPosition = it.location.clone().add(direction.multiply(-20))
                it.world.spawnEntity(spawnPosition, EntityType.PRIMED_TNT)
            }
        }
    }

    override fun run(playerName: String, options: String?, isSilent: Boolean): CommandResultWrapper {
        return super.run(playerName, options, isSilent = true)
    }

}