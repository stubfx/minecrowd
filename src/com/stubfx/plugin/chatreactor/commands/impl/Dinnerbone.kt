package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import org.bukkit.entity.LivingEntity

class Dinnerbone(main: Main) : Command(main) {

    override fun commandName(): String {
        return "dinnerbone"
    }

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer { player ->
            player.getNearbyEntities(100.0, 100.0, 100.0).forEach {
                if (it is LivingEntity) {
                    it.customName = "Dinnerbone"
                    it.isCustomNameVisible = false
                }
            }
        }
    }

}