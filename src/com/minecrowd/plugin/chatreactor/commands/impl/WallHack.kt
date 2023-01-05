package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffectType

object WallHack : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forRandomPlayer { player ->
            player.getNearbyEntities(200.0, 200.0, 200.0).forEach {
                if (it is LivingEntity) {
                    it.addPotionEffect(PotionEffectType.GLOWING.createEffect(ticks * 20, 1))
                }
            }
        }
    }

}