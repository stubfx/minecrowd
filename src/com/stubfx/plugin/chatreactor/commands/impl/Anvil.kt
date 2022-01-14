package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.Material

object Anvil : Command() {

    override fun commandName(): CommandType = CommandType.ANVIL

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            it.location.add(0.0, 5.0, 0.0).block.type = Material.ANVIL
        }
    }

}