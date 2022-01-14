package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command

class ToTheNether(main: Main) : Command(main) {

    override fun commandName(): String {
        return "tothenether"
    }

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            it.teleport(main.server.getWorld("world_nether")?.spawnLocation ?: it.location)
        }
    }

}