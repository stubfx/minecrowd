package com.stubfx.plugin.chatreactor.commands

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.impl.*

enum class CommandType {
    SPAWN, DROPIT, LEVITATE, FIRE,
    DIAMONDS, CHICKENS, KNOCK, PANIC,
    TREE, SPEEDY, HEAL, HUNGRY,
    FEED, WALLHACK, SUPERMAN, NORMALMAN,
    WATER, WOOLLIFY, RANDOMBLOCK, NEVERFALL, ARMORED,
    TOTHENETHER, TOTHEOVERWORLD, BOB, NUKEMOBS,
    DINNERBONE, CRAFTINGTABLE, ANVIL, IHAVEIT,
    PAINT, GOINGDOWN, NOCHUNKNOPARTY, STUB
}

class CommandFactory(val main: Main) {

    private val commandMap: Map<CommandType, Command> = mapOf(
        Pair(CommandType.SPAWN, Spawn(main)),
        Pair(CommandType.DROPIT, DropIt(main)),
        Pair(CommandType.LEVITATE, Levitate(main)),
        Pair(CommandType.FIRE, Fire(main)),
        Pair(CommandType.DIAMONDS, Diamonds(main)),
        Pair(CommandType.CHICKENS, Chickens(main)),
        Pair(CommandType.KNOCK, Knock(main)),
        Pair(CommandType.PANIC, PanicSound(main)),
        Pair(CommandType.TREE, TreeCage(main)),
        Pair(CommandType.SPEEDY, Speedy(main)),
        Pair(CommandType.HEAL, Heal(main)),
        Pair(CommandType.HUNGRY, Hungry(main)),
        Pair(CommandType.FEED, Feed(main)),
        Pair(CommandType.WALLHACK, WallHack(main)),
        Pair(CommandType.SUPERMAN, Superman(main)),
        Pair(CommandType.NORMALMAN, Normalman(main)),
        Pair(CommandType.WATER, Water(main)),
        Pair(CommandType.WOOLLIFY, Woollify(main)),
        Pair(CommandType.RANDOMBLOCK, RandomBlock(main)),
        Pair(CommandType.NEVERFALL, NeverFall(main)),
        Pair(CommandType.ARMORED, Armored(main)),
        Pair(CommandType.TOTHENETHER, ToTheNether(main)),
        Pair(CommandType.TOTHEOVERWORLD, ToTheOverworld(main)),
        Pair(CommandType.BOB, Bob(main)),
        Pair(CommandType.NUKEMOBS, NukeMobs(main)),
        Pair(CommandType.DINNERBONE, Dinnerbone(main)),
        Pair(CommandType.CRAFTINGTABLE, CraftingTable(main)),
        Pair(CommandType.ANVIL, Anvil(main)),
        Pair(CommandType.IHAVEIT, IHaveIt(main)),
        Pair(CommandType.PAINT, Paint(main)),
        Pair(CommandType.GOINGDOWN, GoingDown(main)),
        Pair(CommandType.NOCHUNKNOPARTY, ClearChunk(main))
    )

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