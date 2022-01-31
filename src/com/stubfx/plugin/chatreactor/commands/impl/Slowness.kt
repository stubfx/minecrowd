package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.potion.PotionEffectType

object Slowness : Command() {

    override fun commandType(): CommandType = CommandType.SLOWNESS

    override fun defaultCoolDown(): Long {
        return 1000 * 120
    }

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val speed = PotionEffectType.SLOW.createEffect(ticks * 20, 2)
            it.addPotionEffect(speed)
        }
    }

}