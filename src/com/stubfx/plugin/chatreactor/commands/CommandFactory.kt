package com.stubfx.plugin.chatreactor.commands

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.impl.*

class CommandFactory(val main: Main) {

    companion object {
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
        fun getAvailableCommands() : List<Command> {
            return availableCommands
        }
    }

    private val commandMap: Map<CommandType, Command> = availableCommands.associateBy { it.commandType() }

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
            StubCommand.run()
        }
    }

}