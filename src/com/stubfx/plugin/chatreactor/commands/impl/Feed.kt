package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandType

class Feed(main: Main) : Command(main) {

    override fun commandName(): CommandType = CommandType.FEED

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            it.foodLevel = 100
        }
    }

}