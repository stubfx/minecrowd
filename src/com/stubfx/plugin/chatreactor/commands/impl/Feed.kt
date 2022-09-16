package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner

object Feed : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            it.foodLevel = 100
        }
    }

}