package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.potion.PotionEffectType

object NightVision : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val levitation = PotionEffectType.NIGHT_VISION.createEffect(ticks * 60, 3)
            it.addPotionEffect(levitation)
        }
    }

}