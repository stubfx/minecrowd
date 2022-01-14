package com.stubfx.plugin.chatreactor.commands

import com.stubfx.plugin.CommandConfig
import com.stubfx.plugin.ConfigManager
import com.stubfx.plugin.Main
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*
import kotlin.random.Random

enum class CommandType {
    SPAWN, DROPIT, LEVITATE, FIRE,
    DIAMONDS, CHICKENS, KNOCK, PANIC,
    TREE, SPEEDY, HEAL, HUNGRY,
    FEED, WALLHACK, SUPERMAN, NORMALMAN,
    WATER, WOOLLIFY, RANDOMBLOCK, NEVERFALL, ARMORED,
    TOTHENETHER, TOTHEOVERWORLD, BOB, NUKEMOBS,
    DINNERBONE, CRAFTINGTABLE, ANVIL, IHAVEIT,
    PAINT, GOINGDOWN, NOCHUNKNOPARTY
}

data class CommandResultWrapper(val name: String, val result: Boolean, val message: String)

abstract class Command(val main: Main) {

    var ticks = main.getTicks()
    var commandConfig: CommandConfig = ConfigManager.getCommand(this.commandName())

    companion object {
        var coolDown : Long = 0 // will be overridden by defaultCoolDown function
        var lastRunEpoch: Long = 0
    }

    init {
        coolDown = this.defaultCoolDown()
    }

    open fun defaultCoolDown() : Long {
        return 10 * 1000 // standard coolDown in seconds
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

    abstract fun commandName(): String

    abstract fun behavior(playerName: String, options: String?)

    private fun showTitle(playerName: String) {
        forEachPlayer {
            it.sendTitle(title(), playerName, 10, 70, 20) // ints are def values
        }
    }

    open fun title() : String = commandConfig.title
    open fun successMessage() : String = commandConfig.successMessage

    fun isInCoolDown() : Boolean {
        return Date().time <= (lastRunEpoch + coolDown)
    }

    open fun run(playerName: String) : CommandResultWrapper {
        return run(playerName, "", ConfigManager.isSilent(commandName()))
    }

    open fun run(isSilent: Boolean) : CommandResultWrapper {
        return run("", "", ConfigManager.isSilent(commandName()))
    }

    fun run(playerName: String = "ERROR", options: String? = "", isSilent : Boolean) : CommandResultWrapper {
        coolDown = ConfigManager.getCooldown(commandName())
        var run = false
        val time = Date().time
        if (!isInCoolDown()) {
            lastRunEpoch = time
            println("[ChatReactor] : Running command ${commandName()} - silent: $isSilent")
            run = true
            CommandRunner.runOnBukkit {
                behavior(playerName, options)
            }
            if (!isSilent) showTitle(playerName)
        }
        val msg = if (!run) "@${playerName} ,${commandName()} command is in cooldown" else successMessage()
        return CommandResultWrapper(commandName(), run, msg)
    }

}