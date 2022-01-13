package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import org.bukkit.entity.EntityType

class Chickens(main: Main, playerName: String) : Command(main, playerName) {

    override fun name(): String {
        return "Chickens"
    }

    override fun behavior() {
        forEachPlayer {
            for (i in 0..20) {
                it.world.spawnEntity(it.location, EntityType.CHICKEN)
            }
        }
    }

}