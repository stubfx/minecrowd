package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import org.bukkit.potion.PotionEffectType

class Speedy(main: Main, playerName: String) : Command(main, playerName) {

    override fun name(): String {
        return "speedy"
    }

    override fun behavior() {
        forEachPlayer {
            val speed = PotionEffectType.SPEED.createEffect(ticks * 20, 50)
            it.addPotionEffect(speed)
        }
    }

}