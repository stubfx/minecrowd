package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class Armored(main: Main) : Command(main) {

    override fun commandName(): String {
        return "armored"
    }

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            it.inventory.helmet = ItemStack(Material.NETHERITE_HELMET)
            it.inventory.chestplate = ItemStack(Material.NETHERITE_CHESTPLATE)
            it.inventory.leggings = ItemStack(Material.NETHERITE_LEGGINGS)
            it.inventory.boots = ItemStack(Material.NETHERITE_BOOTS)
            it.inventory.setItemInMainHand(ItemStack(Material.NETHERITE_SWORD))
        }
    }

}