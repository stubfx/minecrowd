package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.Material

object Water : Command() {

    override fun commandName(): CommandType = CommandType.WATER

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            it.location.block.type = Material.WATER
        }
    }

}