package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.potion.PotionEffectType

object Speedy : Command() {

    override val cost: Int = super.cost + 100
    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forRandomPlayer {
            val speed = PotionEffectType.SPEED.createEffect(ticks * 40, 50)
            it.addPotionEffect(speed)
        }
    }

}