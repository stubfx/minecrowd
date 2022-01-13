package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command

class Knock(main: Main, playerName: String) : Command(main, playerName) {

    override fun name(): String {
        return "Knock"
    }

    override fun behavior() {
        forEachPlayer {
            it.velocity = it.location.direction.multiply(-2)
        }
    }

}