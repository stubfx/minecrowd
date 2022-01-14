package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command

class ToTheOverworld(main: Main) : Command(main) {

    override fun commandName(): String {
        return "totheoverworld"
    }

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            it.teleport(main.server.getWorld("world")?.spawnLocation ?: it.location)
        }
    }

}