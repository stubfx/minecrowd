package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.Material

class Water(main: Main) : Command(main) {

    override fun commandName(): CommandType = CommandType.WATER

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            it.location.block.type = Material.WATER
        }
    }

}