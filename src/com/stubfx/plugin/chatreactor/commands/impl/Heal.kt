package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import org.bukkit.potion.PotionEffectType

class Heal(main: Main, playerName: String) : Command(main, playerName) {

    override fun name(): String {
        return "heal"
    }

    override fun behavior() {
        forEachPlayer {
            val heal = PotionEffectType.HEAL.createEffect(ticks * 20, 50)
            it.addPotionEffect(heal)
        }
    }

}