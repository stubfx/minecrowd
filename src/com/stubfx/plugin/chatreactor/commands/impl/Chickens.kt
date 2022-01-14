package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.entity.EntityType

class Chickens(main: Main) : Command(main) {

    override fun commandName(): CommandType = CommandType.CHICKENS

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            for (i in 0..20) {
                it.world.spawnEntity(it.location, EntityType.CHICKEN)
            }
        }
    }

    override fun title(): String = "Chickens!"

}