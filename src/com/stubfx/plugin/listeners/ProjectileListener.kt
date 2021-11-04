package com.stubfx.plugin.listeners

import com.stubfx.plugin.Main
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent

class ProjectileListener(val main: Main) : Listener {

    @EventHandler
    fun onEntityDamageByEntity(event: ProjectileHitEvent) {
        // aight.
    }

}