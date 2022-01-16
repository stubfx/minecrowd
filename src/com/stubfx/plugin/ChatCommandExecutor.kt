package com.stubfx.plugin

import com.stubfx.plugin.chatreactor.commands.CommandFactory
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
        val commandType = CommandType.valueOf(args[0])
        // first of all, get current command config.
        val commandConfig = ConfigManager.getCommand(commandType)
        // what's the property that we are trying to change?
        val commandProperty = ChatCommandProperties.valueOf(args[1].uppercase())
        // are we trying to run the command?
        if (commandProperty == ChatCommandProperties.RUN) {
            // in this case the user is trying to force run the command
            CommandFactory.forceRun(commandType.toString(), sender.name, if (args.size > 2) args[2] else "")
            return
        }
        if (args.size < 3) {
            sender.sendMessage("Missing command argument")
            return
        }
        val newValue = args.slice(2 until args.size).joinToString(" ")
        when (commandProperty) {
            ChatCommandProperties.TITLE -> commandConfig.title = newValue
            ChatCommandProperties.COOLDOWN -> commandConfig.coolDown = newValue.toInt() * 1000L
            ChatCommandProperties.SILENT -> commandConfig.silent = newValue.toBoolean()
            ChatCommandProperties.ENABLED -> commandConfig.enabled = newValue.toBoolean()
            ChatCommandProperties.SHOWSUCCESSMESSAGE -> commandConfig.showSuccessMessage = newValue.toBoolean()
            ChatCommandProperties.SUCCESSMESSAGE -> commandConfig.successMessage = newValue
            else -> throw Exception("Illegal property.")
        }
        ConfigManager.updateCommand(commandConfig)
    }

}