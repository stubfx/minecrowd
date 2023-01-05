package com.minecrowd.plugin.chatreactor.commands.impl.animals

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.entity.EntityType

object Wolves : Command() {

    override val defaultCoolDown: Long = 100 * 1000
    override val cost: Int = 100

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forRandomPlayer {
            for (i in 1..5) {
                val wolf = it.world.spawnEntity(it.location, EntityType.WOLF) as org.bukkit.entity.Wolf
                wolf.owner = it
            }
        }
    }

}