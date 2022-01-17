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
        ThatIsTNT, TunnelTime, OpenSpace, UpsideDown, OnTheMoon,
        Cookies, SuperTools
    )

    fun getAvailableCommandsNames(): List<String> {
        return availableCommands.map { it.commandType().toString() }
    }

    fun getAvailableCommands(): List<Command> {
        return availableCommands
    }

    private val commandMap: Map<CommandType, Command> = availableCommands.associateBy { it.commandType() }

    private fun getCommandType(commandName: String): CommandType {
        return try {
            CommandType.valueOf(commandName.uppercase())
        } catch (_: Exception) {
            CommandType.STUB
        }
    }

    private fun getCommand(commandName: String): Command {
        return getCommand(getCommandType(commandName))
    }

    private fun getCommand(commandName: CommandType): Command {
        return commandMap[commandName] ?: StubCommand
    }

    fun getCommandOptions(commandName: String): List<String> {
        return getCommandOptions(getCommandType(commandName))
    }

    fun getCommandOptions(commandType: CommandType): List<String> {
        return commandMap[commandType]?.options() ?: StubCommand.options()
    }

    fun run(commandName: String, playerName: String, options: String?): CommandResultWrapper {
        return getCommand(commandName).run(playerName, options)
    }

    fun forceRun(commandName: String, playerName: String, options: String?): CommandResultWrapper {
        return getCommand(commandName).forceRun(playerName, options)
    }

}