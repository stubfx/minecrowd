package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

class GoingDown(main: Main, playerName: String) : Command(main, playerName) {

    override fun name(): String {
        return "GoingDown"
    }

    override fun behavior() {
        CommandRunner.startShortRecurrentTask {
            forEachPlayer {
                it.location.subtract(0.0, 1.0, 0.0).block.type = Material.AIR
            }
        }
    }

}