package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import org.bukkit.potion.PotionEffectType

class Levitate(main: Main) : Command(main) {

    override fun commandName(): String {
        return "Levitate"
    }

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            val levitation = PotionEffectType.LEVITATION.createEffect(ticks * 5, 3)
            it.addPotionEffect(levitation)
        }
    }

}