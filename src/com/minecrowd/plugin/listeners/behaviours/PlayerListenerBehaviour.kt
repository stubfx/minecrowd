package com.minecrowd.plugin.listeners.behaviours

import com.minecrowd.plugin.Main
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Fireball
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.Plugin

object PlayerListenerBehaviour {

    fun onOPInteractEvent(main: Main, event: PlayerInteractEvent) {
        // if player is not op, just quit.
        if (!event.player.isOp) return
        val player = event.player
        when (player.inventory.itemInMainHand.type) {
            else -> {
                // just do nothing for the moment.
            }
        }
    }

    fun onPlayerInteractEvent(main: Plugin, event: PlayerInteractEvent) {
        val player = event.player
        when (player.inventory.itemInMainHand.type) {
            Material.GOLDEN_HOE -> {
                if (event.action == Action.LEFT_CLICK_AIR) {
                    player.launchProjectile(Fireball::class.java).velocity = player.location.direction.multiply(0.5)
                    if (player.gameMode != GameMode.CREATIVE) player.inventory.itemInMainHand.amount -= 1
                }
            }

            else -> {
                // just do nothing for the moment.
            }
        }
    }

}