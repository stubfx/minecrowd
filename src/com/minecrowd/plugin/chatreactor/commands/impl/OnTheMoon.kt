package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.potion.PotionEffectType


object OnTheMoon : Command() {


    override fun behavior(playerName: String, options: String?) {
        val duration = ticks * 20
        val jump = PotionEffectType.JUMP.createEffect(duration, 3)
        val slowFalling = PotionEffectType.SLOW_FALLING.createEffect(duration, 50)
        CommandRunner.forEachPlayer {
            it.addPotionEffect(jump)
            it.addPotionEffect(slowFalling)
        }
    }

}