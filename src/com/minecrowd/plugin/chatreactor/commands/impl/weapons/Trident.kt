package com.minecrowd.plugin.chatreactor.commands.impl.weapons

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack


object Trident : Command() {

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val weapon = ItemStack(Material.TRIDENT)
            weapon.addEnchantment(Enchantment.LOYALTY, 3)
            it.inventory.setItemInMainHand(weapon)
        }
    }

}