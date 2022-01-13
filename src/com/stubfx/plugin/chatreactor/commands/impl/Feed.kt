package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command

class Feed(main: Main, playerName: String) : Command(main, playerName) {

    override fun name(): String {
        return "feed"
    }

    override fun behavior() {
        forEachPlayer {
            it.foodLevel = 100
        }
    }

}