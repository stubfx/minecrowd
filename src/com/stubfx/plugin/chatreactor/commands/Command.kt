package com.stubfx.plugin.chatreactor.commands

import com.stubfx.plugin.CommandConfig
import com.stubfx.plugin.ConfigManager
import com.stubfx.plugin.Main
import org.bukkit.Location
import java.util.*
import kotlin.random.Random

enum class CommandType {
    STUB, SPAWN, DROPIT, LEVITATE, FIRE,
    DIAMONDS, CHICKENS, KNOCK, PANIC,
    TREE, SPEEDY, HEAL, HUNGRY,
    FEED, WALLHACK, SUPERMAN, NORMALMAN,
    WATER, WOOLLIFY, RANDOMBLOCK, NEVERFALL, ARMORED,
    TOTHENETHER, TOTHEOVERWORLD, BOB, NUKEMOBS,
    DINNERBONE, CRAFTINGTABLE, ANVIL, IHAVEIT,
    PAINT, GOINGDOWN, NOCHUNKNOPARTY, THATSTNT,
    TUNNELTIME, OPENSPACE, UPSIDEDOWN, ONTHEMOON,
    COOKIES, SUPERTOOLS, MILK, POTION

}

data class CommandResultWrapper(
    val name: CommandType,
    val result: Boolean,
    val message: String,
)

abstract class Command {

    var ticks = 20 // this is supposed to be a minecraft constant.
    private lateinit var commandConfig: CommandConfig
    var coolDown: Long = 0 // will be overridden by defaultCoolDown function
    var lastRunEpoch: Long = 0

    lateinit var main: Main

    init {
        coolDown = this.defaultCoolDown()
        // do not do this
        // commandConfig = ConfigManager.getCommand(this.commandType())
        // as we want fresh data on every run cause the player may have updated it.
    }


    open fun defaultCoolDown(): Long {
        return 30 * 1000 // standard coolDown in seconds
    }

    fun getCloseLocationFromPlayer(location: Location, radius: Double, below : Boolean = false): Location {
        val x = Random.nextDouble(-radius, radius)
        val y = Random.nextDouble(if (below) radius else 1.0, radius)
        val z = Random.nextDouble(-radius, radius)
        return location.add(x, y, z)
    }

    abstract fun commandType(): CommandType

    open fun options(): List<String> {
        return listOf()
    }

    abstract fun behavior(playerName: String, options: String?)

    private fun showTitle(playerName: String) {
        CommandRunner.forEachPlayer {
            it.sendTitle(title(), playerName, 10, 70, 20) // ints are def values
        }
    }

    open fun title(): String = commandConfig.title
    open fun successMessage(): String? = commandConfig.successMessage

    fun isInCoolDown(): Boolean {
        return Date().time <= (lastRunEpoch + coolDown)
    }

    open fun run(playerName: String = "ERROR", options: String? = "", isSilent: Boolean = false): CommandResultWrapper {
        commandConfig = ConfigManager.getCommand(this.commandType())
        val isCommandSilent: Boolean = isSilent
        coolDown = commandConfig.coolDown
        var run = false
        val time = Date().time
        if (!isInCoolDown()) {
            lastRunEpoch = time
            println("[ChatReactor] : Running command ${commandType()} - silent: $isCommandSilent")
            run = true
            runBehavior(playerName, options)
            if (!isCommandSilent) showTitle(playerName)
        }
        val msg = if (!run) "@${playerName} ,${commandType()} command is in cooldown" else successMessage() ?: ""
        return CommandResultWrapper(commandConfig.type, run, msg)
    }

    fun forceRun(playerName: String = "ERROR", options: String? = "", isSilent: Boolean = false): CommandResultWrapper {
        commandConfig = ConfigManager.getCommand(this.commandType())
        runBehavior(playerName, options)
        if (!isSilent) {
            showTitle(playerName)
        }
        return CommandResultWrapper(commandConfig.type, true, "")
    }

    private fun runBehavior(playerName: String, options: String?) {
        CommandRunner.runOnBukkit {
            behavior(playerName, options)
        }
    }

}