package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffectType

class WallHack(main: Main) : Command(main) {

    override fun commandName(): String {
        return "wallhack"
    }

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer { player ->
            player.getNearbyEntities(200.0, 200.0, 200.0).forEach {
                if (it is LivingEntity) {
                    it.addPotionEffect(PotionEffectType.GLOWING.createEffect(ticks * 20, 1))
                }
            }
        }
    }

}