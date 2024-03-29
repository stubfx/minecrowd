package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.potion.PotionEffectType

object Slowness : Command() {


    override val defaultCoolDown: Long = 1000 * 120
    override val cost: Int = 100

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val speed = PotionEffectType.SLOW.createEffect(ticks * 20, 2)
            it.addPotionEffect(speed)
        }
    }

}