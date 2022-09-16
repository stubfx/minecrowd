package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import org.bukkit.entity.LivingEntity

object NukeMobs : Command() {


    override fun defaultCoolDown(): Long {
        return 180 * 1000
    }

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer { player ->
            player.getNearbyEntities(100.0, 100.0, 100.0).forEach {
                if (it is LivingEntity) {
                    it.health = 0.0
                }
            }
        }
    }

}