package com.stubfx.plugin.chatreactor.commands

import com.stubfx.plugin.ConfigManager
import com.stubfx.plugin.Main
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.*
import kotlin.random.Random
import kotlin.time.milliseconds

data class CommandResultWrapper(val name: String, val result: Boolean, val message: String)

abstract class Command(val main: Main, val playerName: String) {

    var ticks = main.getTicks()

    companion object {
        var coolDown : Long = 10 * 1000 // standard cooldown in seconds
        var lastRunEpoch: Long = 0
    }

    fun forEachPlayer(func: (player: Player) -> Unit) {
        main.server.onlinePlayers.forEach { func(it) }
    }

    fun getCloseLocationFromPlayer(location: Location): Location {
        val x = Random.nextDouble(-10.0, 10.0)
        val y = Random.nextDouble(1.0, 10.0)
        val z = Random.nextDouble(-10.0, 10.0)
        return location.add(x, y, z)
    }

    abstract fun name(): String

    abstract fun behavior()

    private fun showTitle() {
        forEachPlayer {
            it.sendTitle(title(), playerName, 10, 70, 20) // ints are def values
        }
    }

    open fun title() : String = ConfigManager.getTitle(name())!!
    open fun successMessage() : String = ""

    fun isInCoolDown() : Boolean {
        return Date().time <= (lastRunEpoch + coolDown)
    }

    open fun run() : CommandResultWrapper {
        return run(ConfigManager.isSilent(name()))
    }

    fun run(isSilent : Boolean) : CommandResultWrapper {
        coolDown = ConfigManager.getCooldown(name())
        var run = false
        val time = Date().time
        if (!isInCoolDown()) {
            lastRunEpoch = time
            println("[ChatReactor] : Running command ${name()} - silent: $isSilent")
            run = true
            CommandRunner.runOnBukkit {
                behavior()
            }
            if (!isSilent) showTitle()
        }
        val msg = if (!run) "@${playerName} ,${name()} command is in cooldown" else successMessage()
        return CommandResultWrapper(name(), run, msg)
    }

}