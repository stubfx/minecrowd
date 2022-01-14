package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command

class Knock(main: Main) : Command(main) {

    override fun commandName(): String {
        return "Knock"
    }

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            it.velocity = it.location.direction.multiply(-2)
        }
    }

}