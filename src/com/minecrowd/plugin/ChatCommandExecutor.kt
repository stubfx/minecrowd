package com.minecrowd.plugin

import com.minecrowd.plugin.chatreactor.commands.CommandFactory
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

object ChatCommandExecutor {

    fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!ConfigManager.isChatReactorEnabled()) {
            return false
        }
        when (command.name.lowercase()) {
            "command" -> {
                onCommandConfigUpdate(sender, args)
            }
        }
        return false
    }

    private fun onCommandConfigUpdate(sender: CommandSender, args: Array<String>) {
        val commandName = args[0]
        // first of all, get current command config.
        val commandConfig = ConfigManager.getCommand(commandName)
        // what's the property that we are trying to change?
        // void index out of bound exception
        val commandProperty = if (args.size > 1) ChatCommandProperties.valueOf(args[1].uppercase()) else null
        // are we trying to run the command?
        if (commandProperty == null || commandProperty == ChatCommandProperties.RUN) {
            // in this case the user is trying to force run the command
            CommandFactory.forceRun(commandName, sender.name, if (args.size > 2) args[2] else "")
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