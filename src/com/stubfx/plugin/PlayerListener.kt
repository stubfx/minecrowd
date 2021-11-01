package com.stubfx.plugin

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent


class PlayerListener(val main: Main) : Listener {

//    @EventHandler
//    fun onPlayerJoin(event: PlayerJoinEvent?) {
//
//    }

    @EventHandler
    fun onPlayerEvent(event: PlayerInteractEvent) {
        val player = event.player
        // if player is OP, golden shovel will clear the whole chunk.
        if (player.isOp) {
            if (player.inventory.itemInMainHand.type == Material.GOLDEN_SHOVEL) {
                val targetBlockExact = player.getTargetBlockExact(20)
                if (targetBlockExact != null) {
                    main.clearChunkListener(targetBlockExact.location, null, true)
                }
            }
        }
    }

}