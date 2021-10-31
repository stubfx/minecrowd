package com.stubfx.plugin;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyMaterialTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return Stream.of(Material.values())
                .map(Material::toString)
//                .map(x -> x.replace("LEGACY_", "")) // remember to check for the api-version in the plugin.yml
                .filter(x -> x.toLowerCase().contains(strings[strings.length-1]))
//                .map(x -> "minecraft:" + x.toLowerCase())
                .collect(Collectors.toList());
    }
}