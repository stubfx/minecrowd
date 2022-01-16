package com.stubfx.plugin.chatreactor.commands

import com.stubfx.plugin.CommandConfig
import com.stubfx.plugin.ConfigManager
import com.stubfx.plugin.Main
import org.bukkit.Location
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
    PAINT, GOINGDOWN, NOCHUNKNOPARTY, STUB, THATSTNT, TUNNELTIME, OPENSPACE, UPSIDEDOWN, ONTHEMOON
}

data class CommandResultWrapper(val name: CommandType, val result: Boolean, val message: String, val successMessage: String? = null)

abstract class Command {

    var ticks = 20 // this is supposed to be a minecraft constant.
    private lateinit var commandConfig: CommandConfig
    var coolDown : Long = 0 // will be overridden by defaultCoolDown function
    var lastRunEpoch: Long = 0

    lateinit var main : Main

    init {
        coolDown = this.defaultCoolDown()
    }

    open fun defaultCoolDown() : Long {
        return 10 * 1000 // standard coolDown in seconds
    }

    fun getCloseLocationFromPlayer(location: Location): Location {
        val x = Random.nextDouble(-10.0, 10.0)
        val y = Random.nextDouble(1.0, 10.0)
        val z = Random.nextDouble(-10.0, 10.0)
        return location.add(x, y, z)
    }

    abstract fun commandType(): CommandType

    abstract fun behavior(playerName: String, options: String?)

    private fun showTitle(playerName: String) {
        CommandRunner.forEachPlayer {
            it.sendTitle(title(), playerName, 10, 70, 20) // ints are def values
        }
    }

    open fun title() : String = commandConfig.title
    open fun successMessage() : String? = commandConfig.successMessage

    fun isInCoolDown() : Boolean {
        return Date().time <= (lastRunEpoch + coolDown)
    }

    open fun run(playerName: String = "ERROR", options: String? = "", isSilent : Boolean? = null) : CommandResultWrapper {
        commandConfig = ConfigManager.getCommand(this.commandType())
        val isCommandSilent : Boolean = isSilent ?: commandConfig.silent
        coolDown = commandConfig.coolDownMillis
        var run = false
        val time = Date().time
        if (!isInCoolDown()) {
            lastRunEpoch = time
            println("[ChatReactor] : Running command ${commandType()} - silent: $isCommandSilent")
            run = true
            CommandRunner.runOnBukkit {
                behavior(playerName, options)
            }
            if (!isCommandSilent) showTitle(playerName)
        }
        val msg = if (!run) "@${playerName} ,${commandType()} command is in cooldown" else successMessage() ?: ""
        return CommandResultWrapper(commandConfig.name, run, msg)
    }

}