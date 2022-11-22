package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object Elytra : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            it.inventory.chestplate = ItemStack(Material.ELYTRA)
            it.inventory.addItem(ItemStack(Material.FIREWORK_ROCKET, 64))
        }
    }

}