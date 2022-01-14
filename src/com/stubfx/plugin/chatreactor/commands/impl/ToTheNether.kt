package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandType

class ToTheNether(main: Main) : Command(main) {

    override fun commandName(): CommandType = CommandType.TOTHENETHER

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            it.teleport(main.server.getWorld("world_nether")?.spawnLocation ?: it.location)
        }
    }

}