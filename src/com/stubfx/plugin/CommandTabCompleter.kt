package com.stubfx.plugin

import com.stubfx.plugin.chatreactor.commands.CommandFactory
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

enum class OptionType {
    STRING, BOOLEAN, INT
}

class CommandTabCompleter : TabCompleter {

    private val options: Map<String, OptionType> = mapOf(
        Pair("title", OptionType.STRING),
        Pair("coolDown", OptionType.INT),
        Pair("enabled", OptionType.BOOLEAN),
        Pair("showSuccessMessage", OptionType.STRING),
    )

    override fun onTabComplete(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): List<String> {
        return when (strings.size) {
            1 -> CommandFactory.getAvailableCommands()
                .map { it.commandType().toString() }
                .filter { it.contains(strings[0]) }
            2 -> listOf("title", "coolDown", "enabled", "showSuccessMessage")
            3 -> optionByPreviousCommand(strings[1])
            else -> listOf("")
        }
    }

    private fun optionByPreviousCommand(prevCommand: String): List<String> {
        return when (options[prevCommand]) {
            OptionType.BOOLEAN -> listOf("true", "false")
            else -> listOf()
        }
    }
}