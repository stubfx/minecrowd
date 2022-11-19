package com.minecrowd.plugin.chatreactor.commands

import com.minecrowd.plugin.CommandConfig
import com.minecrowd.plugin.ConfigManager
import com.minecrowd.plugin.Main
import org.bukkit.Location
import java.util.*
import kotlin.random.Random

data class CommandResultWrapper(
    val name: String,
    val result: Boolean, // true if the command has/will run. False otherwise
    val message: String,
    val showResultMessage: Boolean = false,
)

abstract class Command {
    open val showSuccessMessage: Boolean? = null
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

    fun commandName(): String {
        return this::class.toString().substringAfterLast(".")
    }


    open fun defaultCoolDown(): Long {
        return 30 * 1000 // standard coolDown in seconds
    }

    fun getCloseLocation(location: Location, radius: Double, below: Boolean = false): Location {
        val x = Random.nextDouble(-radius, radius)
        val y = Random.nextDouble(if (below) radius else 1.0, radius)
        val z = Random.nextDouble(-radius, radius)
        return location.add(x, y, z)
    }

    open fun tabCompleterOptions(): List<String> {
        return listOf()
    }

    private fun checkAndSchedule(
        playerName: String, options: String?, isSilent: Boolean = false
    ): CommandResultWrapper {
        commandConfig = ConfigManager.getCommand(this.commandName())
        val isCommandSilent: Boolean = isSilent
        coolDown = commandConfig.coolDown
        val time = Date().time
        if (!isEnabled()) {
            // command is not enabled.
            return resultWrapper(false, "@$playerName command ${commandName().lowercase()} is not enabled.")
        }
        if (isInCoolDown()) {
            // command is in coolDown
            return resultWrapper(false, "@${playerName} ,${commandName()} command is in coolDown")
        }
        val setupResult = setup(playerName, options)
        if (!setupResult.result) {
            return setupResult
        }
        // update last run epoch
        lastRunEpoch = time
        // print in console cause why not
        println("[ChatReactor] : Running command ${commandName()} - silent: $isCommandSilent")
        // is this a silent fart that will kill someone?
        if (!isCommandSilent) showTitle(playerName)
        // aight, now we need to schedule the command
        startCommandBehavior(playerName, options)
        // command will run in the next tick (usually 1/20 of a sec)
        return CommandResultWrapper(commandConfig.name, true, successMessage(),
            showSuccessMessage ?: commandConfig.showSuccessMessage)
    }

    /**
     * This method is called in the main thread that the command is running into, this is because the command behavior will
     * run in a separate one. Usually the behavior runs after the title cause is scheduled in a Bukkit thread,
     * this means that if we try to access some lateinit variables in the title function we will get an error.
     *
     * Remember to use this method if there are variables shared between the title and the behavior of the same command.
     */
    open fun setup(playerName: String, options: String?): CommandResultWrapper {
        // commands may not require any setup, therefore, the standard one is always true.
        return resultWrapper(true, successMessage())
    }

    /**
     * This method is run in a separated Bukkit thread, therefore it usually runs after the title function.
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
        println(Date().time)
        println(lastRunEpoch + coolDown)
        return Date().time <= (lastRunEpoch + coolDown)
    }

    open fun run(playerName: String = "ERROR", options: String? = "", isSilent: Boolean = false): CommandResultWrapper {
        return checkAndSchedule(playerName, options, isSilent)
    }

    private fun isEnabled(): Boolean {
        return commandConfig.enabled
    }

    fun forceRun(playerName: String = "ERROR", options: String? = "", isSilent: Boolean = false): CommandResultWrapper {
        commandConfig = ConfigManager.getCommand(this.commandName())
        // no checks, just party up!
        startCommandBehavior(playerName, options)
        if (!isSilent) {
            showTitle(playerName)
        }
        return CommandResultWrapper(commandConfig.name, true, "")
    }

    private fun startCommandBehavior(playerName: String, options: String?) {
        CommandRunner.clearAllDroppedItems()
        CommandRunner.runOnBukkit {
            behavior(playerName, options)
        }
    }

    fun resultWrapper(result: Boolean, msg: String, showSuccessMessage: Boolean = true): CommandResultWrapper {
        return CommandResultWrapper(commandName(), result, msg, showSuccessMessage)
    }

}