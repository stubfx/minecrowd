package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command

class Fire(main: Main) : Command(main) {

    override fun commandName(): String {
        return "Fire"
    }

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            it.fireTicks = ticks * 20
        }
    }

}