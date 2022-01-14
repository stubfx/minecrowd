package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

class GoingDown(main: Main) : Command(main) {

    override fun commandName(): String {
        return "GoingDown"
    }

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.startShortRecurrentTask {
            forEachPlayer {
                it.location.subtract(0.0, 1.0, 0.0).block.type = Material.AIR
            }
        }
    }

}