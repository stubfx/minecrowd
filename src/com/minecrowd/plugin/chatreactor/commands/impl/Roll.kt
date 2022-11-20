package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object Roll : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            CommandRunner.forEachPlayer {
                val rndMaterial = ItemStack(listOf(*Material.values()).random(), 1)
                it.inventory.setItemInMainHand(rndMaterial)
            }
        }
    }

}