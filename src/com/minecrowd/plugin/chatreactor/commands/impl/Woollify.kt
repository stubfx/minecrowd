package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.BlockReplacer
import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

object Woollify : Command() {


    override fun behavior(playerName: String, options: String?) {
        val wool: List<Material> = listOf(
            Material.WHITE_WOOL,
            Material.BLUE_WOOL,
            Material.RED_WOOL,
            Material.CYAN_WOOL,
            Material.GRAY_WOOL,
        )
        CommandRunner.forRandomPlayer {
            val loc1 = it.location.subtract(20.0, 20.0, 20.0)
            val loc2 = it.location.add(20.0, 20.0, 20.0)
            BlockReplacer.replaceAreaAsync(loc1, loc2, wool.random())
        }
        // clear stuff on the ground, just to make sure.
        CommandRunner.clearAllDroppedItems()
    }

}