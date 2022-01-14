package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType

object Knock : Command() {

    override fun commandType(): CommandType = CommandType.KNOCK

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            it.velocity = it.location.direction.multiply(-2)
        }
    }

}