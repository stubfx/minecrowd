package com.stubfx.plugin.listeners

import com.stubfx.plugin.Main
import com.stubfx.plugin.listeners.behaviours.PlayerListenerBehaviour
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent


class PlayerListener(private val main: Main) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onOPPlayerEvent(event: PlayerInteractEvent) {
        val player = event.player
        if (player.isOp) {
            PlayerListenerBehaviour.onOPInteractEvent(main, event)
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    fun onPlayerEvent(event: PlayerInteractEvent) {
        PlayerListenerBehaviour.onPlayerInteractEvent(main, event)
    }

}