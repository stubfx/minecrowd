package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command

class Feed(main: Main) : Command(main) {

    override fun commandName(): String {
        return "feed"
    }

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            it.foodLevel = 100
        }
    }

}