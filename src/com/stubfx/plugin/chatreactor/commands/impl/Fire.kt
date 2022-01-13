package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command

class Fire(main: Main, playerName: String) : Command(main, playerName) {

    override fun name(): String {
        return "Fire"
    }

    override fun behavior() {
        forEachPlayer {
            it.fireTicks = ticks * 20
        }
    }

}