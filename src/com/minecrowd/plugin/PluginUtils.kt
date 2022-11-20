package com.minecrowd.plugin

import com.minecrowd.plugin.chatreactor.PointsManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Entity

enum class LogType {
    ERROR,
    WARNING,
    INFO
}

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

    fun log(msg: String, type: LogType = LogType.INFO) {
        val color: ChatColor = when (type) {
            LogType.ERROR -> ChatColor.RED
            LogType.WARNING -> ChatColor.YELLOW
            LogType.INFO -> ChatColor.GRAY
        }

        Bukkit.getConsoleSender().sendMessage("$color[minecrowd] $msg")
    }

    fun teleportToEnd(entity: Entity) {
        entity.teleport(Bukkit.getWorld("world_the_end")?.spawnLocation ?: entity.location)
    }

    fun teleportToNether(entity: Entity) {
        entity.teleport(Bukkit.getWorld("world_nether")?.spawnLocation ?: entity.location)
    }

    fun teleportToOverworld(entity: Entity) {
        entity.teleport(Bukkit.getWorld("world")?.spawnLocation ?: entity.location)
    }

    fun broadcastMessage(msg: String) {
//        var msg = if (PointsManager.lastBroadcastedAmount < PointsManager.currentAmount) {
//            // in this case points should be red
//            "Points : &c${PointsManager.currentAmount}"
//        } else {
//            // in this other case they should be green
//            "Points : &a${PointsManager.currentAmount}"
//        }
//        PointsManager.lastBroadcastedAmount = PointsManager.currentAmount
//        msg = ChatColor.translateAlternateColorCodes('&',msg)
        PointsManager.main.server.broadcastMessage(msg)
    }

}