package com.stubfx.plugin

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

object PluginUtils {

    lateinit var main: Main

    fun setMainRef(mainRef: Main) {
        main = mainRef
    }

    fun checkLocationsWorld(l1: Location, l2: Location) {
        if (l1.world != l2.world) {
            throw IllegalArgumentException("both locations must be from the same world")
        }
    }

    fun getMinLocation(l1: Location, l2: Location): Location {
        checkLocationsWorld(l1, l2)
        return Location(l1.world, minOf(l1.x, l2.x), minOf(l1.y, l2.y), minOf(l1.z, l2.z))
    }

    fun getMaxLocation(l1: Location, l2: Location): Location {
        checkLocationsWorld(l1, l2)
        return Location(l1.world, maxOf(l1.x, l2.x), maxOf(l1.y, l2.y), maxOf(l1.z, l2.z))
    }

    fun log(msg: String, type: String = "") {
        val color: ChatColor = when (type) {
            "error" -> ChatColor.RED
            "warning" -> ChatColor.YELLOW
            "silent" -> ChatColor.GRAY
            else -> ChatColor.AQUA
        }

        Bukkit.getConsoleSender().sendMessage("$color[stubFXplugin] $msg")
    }

    fun teleportToNether(entity : Entity) {
        entity.teleport(main.server.getWorld("world_nether")?.spawnLocation ?: entity.location)
    }

    fun teleportToOverworld(entity : Entity) {
        entity.teleport(main.server.getWorld("world")?.spawnLocation ?: entity.location)
    }

}