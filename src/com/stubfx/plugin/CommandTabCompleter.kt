package com.stubfx.plugin

import com.stubfx.plugin.chatreactor.commands.CommandFactory
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

enum class EditableCommandProperty {
    TYPE,
    TITLE,
    COOLDOWN,
    SILENT,
    ENABLED,
    SHOWSUCCESSMESSAGE,
    SUCCESSMESSAGE
}

enum class OptionType {
    STRING, BOOLEAN, INT
}

class CommandTabCompleter : TabCompleter {

    private val options: Map<EditableCommandProperty, OptionType> = mapOf(
        Pair(EditableCommandProperty.TITLE, OptionType.STRING),
        Pair(EditableCommandProperty.COOLDOWN, OptionType.INT),
        Pair(EditableCommandProperty.SILENT, OptionType.BOOLEAN),
        Pair(EditableCommandProperty.ENABLED, OptionType.BOOLEAN),
        Pair(EditableCommandProperty.SHOWSUCCESSMESSAGE, OptionType.BOOLEAN),
        Pair(EditableCommandProperty.SUCCESSMESSAGE, OptionType.STRING),
    )

    override fun onTabComplete(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): List<String> {
        return when (strings.size) {
            1 -> CommandFactory.getAvailableCommands()
                .map { it.commandType().toString() }
                .filter { it.lowercase().contains(strings[0].lowercase()) }
            2 -> listOf("title", "coolDown", "enabled", "showSuccessMessage")
            3 -> optionByPreviousCommand(strings[1])
            else -> listOf("")
        }
    }

    private fun optionByPreviousCommand(prevCommand: String): List<String> {
        return when (options[EditableCommandProperty.valueOf(prevCommand.uppercase())]) {
            OptionType.BOOLEAN -> listOf("true", "false")
            else -> listOf()
        }
    }
}