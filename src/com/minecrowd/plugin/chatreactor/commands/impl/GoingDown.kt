package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

object GoingDown : Command() {


    override val defaultCoolDown: Long = 60 * 1000
    override val cost: Int = 100

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.startShortRecurrentTask {
            CommandRunner.forEachPlayer {
                it.location.subtract(0.0, 1.0, 0.0).block.type = Material.AIR
            }
        }
    }

}