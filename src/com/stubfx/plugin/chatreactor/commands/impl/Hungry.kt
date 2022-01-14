package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.potion.PotionEffectType

class Hungry(main: Main) : Command(main) {

    override fun commandName(): CommandType = CommandType.HUNGRY

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            it.addPotionEffect(PotionEffectType.HUNGER.createEffect(ticks * 10, 50))
        }
    }

}