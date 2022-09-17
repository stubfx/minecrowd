package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.potion.PotionEffectType

object Speedy : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val speed = PotionEffectType.SPEED.createEffect(ticks * 20, 50)
            it.addPotionEffect(speed)
        }
    }

}