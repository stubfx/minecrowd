package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.potion.PotionEffectType

object Hungry : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            it.addPotionEffect(PotionEffectType.HUNGER.createEffect(ticks * 10, 50))
        }
    }

}