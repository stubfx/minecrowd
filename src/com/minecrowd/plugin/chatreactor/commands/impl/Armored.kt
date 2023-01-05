package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object Armored : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forRandomPlayer {
            it.inventory.helmet = ItemStack(Material.NETHERITE_HELMET)
            it.inventory.chestplate = ItemStack(Material.NETHERITE_CHESTPLATE)
            it.inventory.leggings = ItemStack(Material.NETHERITE_LEGGINGS)
            it.inventory.boots = ItemStack(Material.NETHERITE_BOOTS)
        }
    }

}