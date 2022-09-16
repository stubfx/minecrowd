package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import org.bukkit.potion.PotionEffectType

object Hungry : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            it.addPotionEffect(PotionEffectType.HUNGER.createEffect(ticks * 10, 50))
        }
    }

}