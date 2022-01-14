package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType

object Feed : Command() {

    override fun commandName(): CommandType = CommandType.FEED

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            it.foodLevel = 100
        }
    }

}