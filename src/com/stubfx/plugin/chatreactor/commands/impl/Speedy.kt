package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.potion.PotionEffectType

class Speedy(main: Main) : Command(main) {

    override fun commandName(): CommandType = CommandType.SPEEDY

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            val speed = PotionEffectType.SPEED.createEffect(ticks * 20, 50)
            it.addPotionEffect(speed)
        }
    }

}