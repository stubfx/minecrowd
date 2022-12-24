package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.entity.Bee
import org.bukkit.entity.EntityType


object Bees : Command() {


    override val defaultCoolDown: Long = 1000 * 300

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            for (i in 0..10) {
                val location = getCloseLocation(it.location, 5.0)
                val bee = location.world?.spawnEntity(location, EntityType.BEE) as Bee
                // nah, too many player names
                // bee.customName = playerName
                // bee.isCustomNameVisible = true
                bee.target = it
                bee.anger = 100000
            }
        }
    }

}