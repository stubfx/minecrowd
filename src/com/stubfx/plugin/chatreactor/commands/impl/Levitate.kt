package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import org.bukkit.potion.PotionEffectType

class Levitate(main: Main, playerName: String) : Command(main, playerName) {

    override fun name(): String {
        return "Levitate"
    }

    override fun behavior() {
        forEachPlayer {
            val levitation = PotionEffectType.LEVITATION.createEffect(ticks * 5, 3)
            it.addPotionEffect(levitation)
        }
    }

}