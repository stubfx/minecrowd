package com.minecrowd.plugin.chatreactor.commands.impl.diamondItems

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object DiamondSword : Command() {

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forRandomPlayer {
            val weapon = ItemStack(Material.DIAMOND_SWORD)
            it.inventory.setItemInMainHand(weapon)
        }
    }

}