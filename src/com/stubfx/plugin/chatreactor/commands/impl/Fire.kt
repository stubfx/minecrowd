package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandType

class Fire(main: Main) : Command(main) {

    override fun commandName(): CommandType = CommandType.FIRE

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            it.fireTicks = ticks * 20
        }
    }

}