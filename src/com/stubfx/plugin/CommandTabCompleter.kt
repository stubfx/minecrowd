package com.stubfx.plugin

import com.stubfx.plugin.chatreactor.commands.CommandFactory
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

enum class ChatCommandProperties {
    RUN,
    TITLE,
    COOLDOWN,
    SILENT,
    ENABLED,
    SHOWSUCCESSMESSAGE,
    SUCCESSMESSAGE
}

enum class OptionType {
    RUN, STRING, BOOLEAN, INT
}

class CommandTabCompleter : TabCompleter {

    private val options: Map<ChatCommandProperties, OptionType> = mapOf(
        Pair(ChatCommandProperties.RUN, OptionType.RUN),
        Pair(ChatCommandProperties.TITLE, OptionType.STRING),
        Pair(ChatCommandProperties.COOLDOWN, OptionType.INT),
        Pair(ChatCommandProperties.SILENT, OptionType.BOOLEAN),
        Pair(ChatCommandProperties.ENABLED, OptionType.BOOLEAN),
        Pair(ChatCommandProperties.SHOWSUCCESSMESSAGE, OptionType.BOOLEAN),
        Pair(ChatCommandProperties.SUCCESSMESSAGE, OptionType.STRING),
    )

    override fun onTabComplete(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): List<String> {
        return try {
            when (strings.size) {
                1 -> CommandFactory.getAvailableCommands()
                    .map { it.commandType().toString() }
                    .filter { it.lowercase().contains(strings[0].lowercase()) }
                2 -> ChatCommandProperties.values().map { it.toString().lowercase() }
                3 -> optionByPreviousCommand(strings[2], strings[1], CommandFactory.getCommandOptions(strings[0]))
                else -> listOf("")
            }
        } catch (_: Exception) {
            PluginUtils.log("[Chat Reactor] Invalid command option.", LogType.WARNING)
            listOf()
        }
    }

    private fun optionByPreviousCommand(currentInput: String, prevOption: String, commandOptions: List<String>): List<String> {
        return when (options[ChatCommandProperties.valueOf(prevOption.uppercase())]) {
            OptionType.BOOLEAN -> listOf("true", "false")
            OptionType.RUN -> commandOptions.filter { it.lowercase().contains(currentInput.lowercase()) }
            else -> listOf()
        }
    }
}