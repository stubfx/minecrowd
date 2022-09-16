package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

object GoingDown : Command() {


    override fun defaultCoolDown(): Long {
        return 60 * 1000
    }

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.startShortRecurrentTask {
            CommandRunner.forEachPlayer {
                it.location.subtract(0.0, 1.0, 0.0).block.type = Material.AIR
            }
        }
    }

}