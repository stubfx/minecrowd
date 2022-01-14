package com.stubfx.plugin.chatreactor.commands

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.impl.*

class CommandFactory(val main: Main) {

    companion object {
        fun getAvailableCommands() : List<CommandType> {
            val list = CommandType.values().toMutableList()
            list.remove(CommandType.STUB)
            return list
        }
    }

    private val availableCommands = listOf(
        Spawn, DropIt, Levitate, Anvil,
        Fire, Diamonds, Chickens, Knock,
        PanicSound, TreeCage, Speedy, Heal,
        Hungry, Feed, WallHack, Superman,
        Normalman, Water, Woollify, RandomBlock,
        NeverFall, Armored, ToTheNether, ToTheOverworld,
        Bob, NukeMobs, Dinnerbone, CraftingTable,
        IHaveIt, Paint, GoingDown, ClearChunk
    )

    private val commandMap: Map<CommandType, Command> = availableCommands.associateBy { it.commandName() }

    fun run(commandName: String, playerName: String, options: String?): CommandResultWrapper {
        val command = try {
            CommandType.valueOf(commandName.uppercase())
        } catch (_: Exception) {
            null
        }
        return try {
            commandMap[command]?.run(playerName, options) ?: StubCommand.run()
        } catch (e: Exception) {
            e.printStackTrace()
            CommandResultWrapper(CommandType.STUB, false, "wrong command.")
        }
    }

}