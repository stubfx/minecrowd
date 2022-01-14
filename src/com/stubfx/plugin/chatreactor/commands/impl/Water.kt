package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import org.bukkit.Material

class Water(main: Main) : Command(main) {

    override fun commandName(): String {
        return "water"
    }

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            it.location.block.type = Material.WATER
        }
    }

}