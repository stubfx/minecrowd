package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import org.bukkit.Material

class Water(main: Main, playerName: String) : Command(main, playerName) {

    override fun name(): String {
        return "water"
    }

    override fun behavior() {
        forEachPlayer {
            it.location.block.type = Material.WATER
        }
    }

}