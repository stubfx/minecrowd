package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.BlockReplacer
import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

object CraftingTable : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val loc1 = it.location.subtract(20.0, 20.0, 20.0)
            val loc2 = it.location.add(20.0, 20.0, 20.0)
            BlockReplacer.replaceArea(loc1, loc2, Material.CRAFTING_TABLE)
        }
    }

}