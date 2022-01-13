package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import org.bukkit.Material

class Anvil(main: Main, playerName: String) : Command(main, playerName) {

    override fun name(): String {
        return "Knock"
    }

    override fun behavior() {
        forEachPlayer {
            it.location.add(0.0, 5.0, 0.0).block.type = Material.ANVIL
        }
    }

}