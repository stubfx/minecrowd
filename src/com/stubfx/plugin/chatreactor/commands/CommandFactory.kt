package com.stubfx.plugin.chatreactor.commands

import com.stubfx.plugin.chatreactor.commands.impl.*

object CommandFactory {

    private val availableCommands = listOf(
        Spawn, DropIt, Levitate, Anvil,
        Fire, Diamonds, Chickens, Knock,
        PanicSound, TreeCage, Speedy, Heal,
        Hungry, Feed, WallHack, Superman,
        Normalman, Water, Woollify, RandomBlock,
        NeverFall, Armored, ToTheNether, ToTheOverworld,
        Bob, NukeMobs, Dinnerbone, CraftingTable,
        IHaveIt, Paint, GoingDown, ClearChunk,
        ThatIsTNT, TunnelTime, OpenSpace, UpsideDown, OnTheMoon
    )

    fun getAvailableCommandsNames(): List<String> {
        return availableCommands.map { it.commandType().toString() }
    }

    fun getAvailableCommands(): List<Command> {
        return availableCommands
    }

    private val commandMap: Map<CommandType, Command> = availableCommands.associateBy { it.commandType() }

    fun getType(commandName: String): CommandType {
        return try {
            CommandType.valueOf(commandName.uppercase())
        } catch (_: Exception) {
            CommandType.STUB // as default.
        }
    }

    fun getCommandOptions(commandType: CommandType): List<String> {
        return commandMap[commandType]?.options() ?: listOf()
    }

    fun run(commandName: String, playerName: String, options: String?): CommandResultWrapper {
        val command = getType(commandName)
        return try {
            commandMap[command]!!.run(playerName, options)
        } catch (e: Exception) {
            e.printStackTrace()
            StubCommand.run(playerName)
        }
    }

    fun forceRun(commandName: String, playerName: String, options: String?): CommandResultWrapper {
        val command = getType(commandName)
        return try {
            commandMap[command]!!.forceRun(playerName, options)
        } catch (e: Exception) {
            e.printStackTrace()
            StubCommand.run(playerName)
        }
    }

}