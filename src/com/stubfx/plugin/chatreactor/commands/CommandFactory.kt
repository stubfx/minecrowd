package com.stubfx.plugin.chatreactor.commands

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.impl.*

class CommandFactory(val main: Main) {

    private val availableCommands = listOf(
        Spawn(main), DropIt(main), Levitate(main), Anvil(main),
        Fire(main), Diamonds(main), Chickens(main), Knock(main),
        PanicSound(main), TreeCage(main), Speedy(main), Heal(main),
        Hungry(main), Feed(main), WallHack(main), Superman(main),
        Normalman(main), Water(main), Woollify(main), RandomBlock(main),
        NeverFall(main), Armored(main), ToTheNether(main), ToTheOverworld(main),
        Bob(main), NukeMobs(main), Dinnerbone(main), CraftingTable(main),
        IHaveIt(main), Paint(main), GoingDown(main), ClearChunk(main),
    )

    private val commandMap: Map<CommandType, Command> = availableCommands.associateBy { it.commandName() }

    fun getAvailableCommands() : Set<CommandType> {
        return commandMap.keys
    }

    fun run(commandName: String, playerName: String, options: String?): CommandResultWrapper {
        val command = try {
            CommandType.valueOf(commandName.uppercase())
        } catch (_: Exception) {
            null
        }
        return try {
            (commandMap[command] ?: StubCommand(main, playerName)).run(playerName, options, false)
        } catch (e: Exception) {
            e.printStackTrace()
            CommandResultWrapper(CommandType.STUB, false, "wrong command.")
        }
    }

}