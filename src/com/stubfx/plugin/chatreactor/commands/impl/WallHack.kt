package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffectType

object WallHack : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer { player ->
            player.getNearbyEntities(200.0, 200.0, 200.0).forEach {
                if (it is LivingEntity) {
                    it.addPotionEffect(PotionEffectType.GLOWING.createEffect(ticks * 20, 1))
                }
            }
        }
    }

}