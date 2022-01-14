package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.BlockReplacer
import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import org.bukkit.Material

class CraftingTable(main: Main) : Command(main) {

    override fun commandName(): String {
        return "craftingtable"
    }

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            val loc1 = it.location.subtract(20.0, 20.0, 20.0)
            val loc2 = it.location.add(20.0, 20.0, 20.0)
            BlockReplacer.replaceAreaExAir(main, loc1, loc2, Material.CRAFTING_TABLE)
        }
    }

}