package com.minecrowd.plugin.listeners

import com.minecrowd.plugin.Main
import com.minecrowd.plugin.listeners.behaviours.PlayerListenerBehaviour
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent


@Suppress("unused")
class PlayerListener(private val main: Main) : Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    fun onPlayerEvent(event: PlayerInteractEvent) {
        PlayerListenerBehaviour.onPlayerInteractEvent(main, event)
    }

}