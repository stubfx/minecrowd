package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import org.bukkit.potion.PotionEffectType

class Hungry(main: Main, playerName: String) : Command(main, playerName) {

    override fun name(): String {
        return "hungry"
    }

    override fun behavior() {
        forEachPlayer {
            it.addPotionEffect(PotionEffectType.HUNGER.createEffect(ticks * 10, 50))
        }
    }

}