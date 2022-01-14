package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.potion.PotionEffectType

object Levitate : Command() {

    override fun commandName(): CommandType = CommandType.LEVITATE

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val levitation = PotionEffectType.LEVITATION.createEffect(ticks * 5, 3)
            it.addPotionEffect(levitation)
        }
    }

}