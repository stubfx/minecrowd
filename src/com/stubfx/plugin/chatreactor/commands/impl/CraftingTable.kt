package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.BlockReplacer
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.Material

object CraftingTable : Command() {

    override fun commandType(): CommandType = CommandType.CRAFTINGTABLE

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val loc1 = it.location.subtract(20.0, 20.0, 20.0)
            val loc2 = it.location.add(20.0, 20.0, 20.0)
            BlockReplacer.replaceAreaExAir(loc1, loc2, Material.CRAFTING_TABLE)
        }
    }

}