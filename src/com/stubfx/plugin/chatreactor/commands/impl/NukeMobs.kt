package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import org.bukkit.entity.LivingEntity

class NukeMobs(main: Main) : Command(main) {

    override fun commandName(): String {
        return "nukemobs"
    }

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer { player ->
            player.getNearbyEntities(100.0, 100.0, 100.0).forEach {
                if (it is LivingEntity) {
                    it.health = 0.0
                }
            }
        }
    }

}