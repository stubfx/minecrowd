package com.stubfx.plugin

import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.util.stream.Collectors
import java.util.stream.Stream

class MyMaterialTabCompleter : TabCompleter {
    override fun onTabComplete(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): List<String>? {
        return Stream.of(*Material.values())
                .map { obj: Material -> obj.toString() } //                .map(x -> x.replace("LEGACY_", "")) // remember to check for the api-version in the plugin.yml
                .filter { x: String -> x.lowercase().contains(strings[strings.size - 1]) } //                .map(x -> "minecraft:" + x.toLowerCase())
                .collect(Collectors.toList())
    }
}