package com.minecrowd.plugin.chatreactor.commands.impl.goldenItems

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object GoldenShovel : Command() {

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val weapon = ItemStack(Material.GOLDEN_SHOVEL)
            it.inventory.setItemInMainHand(weapon)
        }
    }

}