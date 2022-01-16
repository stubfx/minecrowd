package com.stubfx.plugin

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

object ChatCommandExecutor {

    fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        when (command.name.lowercase()) {
            "config" -> {
//                ConfigManager.updateCommand(CommandType.valueOf(args[0]), )
            }
        }
        return false
    }

}