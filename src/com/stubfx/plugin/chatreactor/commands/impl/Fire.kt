package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

object Fire : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            // instead of setting the player on fire, let's put the block below the player on fire
            // so if somebody used woollify before, that's gonna be fun.
            // it.fireTicks = ticks * 20
            it.location.block.type = Material.FIRE
        }
    }

}