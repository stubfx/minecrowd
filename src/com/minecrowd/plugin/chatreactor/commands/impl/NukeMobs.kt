package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.entity.LivingEntity

object NukeMobs : Command() {


    override val defaultCoolDown: Long = 180 * 1000
    override val cost: Int = 200

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forRandomPlayer { player ->
            player.getNearbyEntities(100.0, 100.0, 100.0).forEach {
                if (it is LivingEntity) {
                    it.health = 0.0
                }
            }
        }
    }

}