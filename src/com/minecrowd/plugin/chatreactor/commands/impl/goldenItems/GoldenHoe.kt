package com.minecrowd.plugin.chatreactor.commands.impl.goldenItems

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object GoldenHoe : Command() {

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val goldenHoe = ItemStack(Material.GOLDEN_HOE)
            it.inventory.setItemInMainHand(goldenHoe)
        }
    }

}