package com.stubfx.plugin

import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

object ChatCommandExecutor {

    fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        when (command.name.lowercase()) {
            "command" -> {
                onCommandConfigUpdate(sender, command, label, args)
            }
        }
        return false
    }

    private fun onCommandConfigUpdate(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val type = CommandType.valueOf(args[0])
        // first of all, get current command config.
        val commandConfig = ConfigManager.getCommand(type)
        // what's the property that we are trying to change?
        val commandProperty = EditableCommandProperty.valueOf(args[1].uppercase())
        val newValue = args[2]
        when (commandProperty) {
            EditableCommandProperty.TITLE -> commandConfig.title = newValue
            EditableCommandProperty.COOLDOWN -> commandConfig.coolDown = newValue.toInt() * 1000L
            EditableCommandProperty.SILENT -> commandConfig.silent = newValue.toBoolean()
            EditableCommandProperty.ENABLED -> commandConfig.enabled = newValue.toBoolean()
            EditableCommandProperty.SHOWSUCCESSMESSAGE -> commandConfig.showSuccessMessage = newValue.toBoolean()
            EditableCommandProperty.SUCCESSMESSAGE -> commandConfig.successMessage = newValue
            else -> throw Exception("Illegal property.")
        }
        ConfigManager.updateCommand(commandConfig)
    }

}