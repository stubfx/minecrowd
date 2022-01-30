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
    COOKIES, SUPERTOOLS, MILK, POTION, LAVA

}

data class CommandResultWrapper(
    val name: CommandType,
    val result: Boolean, // true if the command has/will run. False otherwise
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

    /**
     * This method is called in the main thread that the command is running into, this is because the command behavior will
     * run in a separate one. Usually the behavior runs after the title cause is scheduled in a Bukkit thread,
     * this means that if we try to access some lateinit variables in the title function we will get an error.
     *
     * Remember to use this method if there are variables shared between the title and the behavior of the same command.
     */
    open fun preBehavior(playerName: String, options: String?) {
        // congrats, do nothing!
    }

    /**
     * This method is run in a separated Bukkit thread, therefore it usually runs after the title method.
     */
    abstract fun behavior(playerName: String, options: String?)

    private fun showTitle(playerName: String) {
        CommandRunner.forEachPlayer {
            it.sendTitle(title(), playerName, 10, 70, 20) // ints are def values
        }
    }

    open fun title(): String = commandConfig.title
    open fun successMessage(): String = commandConfig.successMessage

    fun isInCoolDown(): Boolean {
        return Date().time <= (lastRunEpoch + coolDown)
    }

    open fun run(playerName: String = "ERROR", options: String? = "", isSilent: Boolean = false): CommandResultWrapper {
        commandConfig = ConfigManager.getCommand(this.commandType())
        val isCommandSilent: Boolean = isSilent
        coolDown = commandConfig.coolDown
        val time = Date().time
        if (!isEnabled()) {
            // command is not enabled.
            val message = "@$playerName command ${commandType().name.lowercase()} is not enabled."
            return CommandResultWrapper(commandConfig.type, false, message)
        }
        if (isInCoolDown()) {
            val message = "@${playerName} ,${commandType()} command is in cooldown"
            return CommandResultWrapper(commandConfig.type, false, message)
        }
        // just run this command ffs
        // update last run epoch
        lastRunEpoch = time
        // print in console cause why not
        println("[ChatReactor] : Running command ${commandType()} - silent: $isCommandSilent")
        // start this lifecycle pls!
        startCommandLifecycle(playerName, options)
        // is this a silent fart that will kill someone?
        if (!isCommandSilent) showTitle(playerName)
        // aight, package is ready!
        return CommandResultWrapper(commandConfig.type, true, successMessage())
    }

    private fun isEnabled(): Boolean {
        return commandConfig.enabled
    }

    fun forceRun(playerName: String = "ERROR", options: String? = "", isSilent: Boolean = false): CommandResultWrapper {
        commandConfig = ConfigManager.getCommand(this.commandType())
        startCommandLifecycle(playerName, options)
        if (!isSilent) {
            showTitle(playerName)
        }
        return CommandResultWrapper(commandConfig.type, true, "")
    }

    private fun startCommandLifecycle(playerName: String, options: String?) {
        preBehavior(playerName, options)
        CommandRunner.runOnBukkit {
            behavior(playerName, options)
        }
    }

}