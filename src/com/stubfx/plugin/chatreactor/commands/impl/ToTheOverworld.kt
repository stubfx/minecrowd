package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command

class ToTheOverworld(main: Main, playerName: String) : Command(main, playerName) {

    override fun name(): String {
        return "totheoverworld"
    }

    override fun behavior() {
        forEachPlayer {
            it.teleport(main.server.getWorld("world")?.spawnLocation ?: it.location)
        }
    }

}