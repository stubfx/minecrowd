package com.stubfx.plugin

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent


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
            main.onPlayerInteractEvent(event)
        }
    }

}