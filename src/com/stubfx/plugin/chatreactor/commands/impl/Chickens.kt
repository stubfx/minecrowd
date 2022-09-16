package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import org.bukkit.entity.EntityType

object Chickens : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            for (i in 0..20) {
                it.world.spawnEntity(it.location, EntityType.CHICKEN)
            }
        }
    }

    override fun title(): String = "Chickens!"

}