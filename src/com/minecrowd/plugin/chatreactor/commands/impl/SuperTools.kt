package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack


object SuperTools : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forRandomPlayer {
            val pick = ItemStack(Material.NETHERITE_PICKAXE, 1)
            pick.addEnchantment(Enchantment.DIG_SPEED, 5)
            it.inventory.addItem(pick)
            val axe = ItemStack(Material.NETHERITE_AXE, 1)
            axe.addEnchantment(Enchantment.DIG_SPEED, 5)
            it.inventory.addItem(axe)
        }
    }

}