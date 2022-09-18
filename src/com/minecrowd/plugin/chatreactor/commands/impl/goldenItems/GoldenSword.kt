package com.minecrowd.plugin.chatreactor.commands.impl.goldenItems

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object GoldenSword : Command() {

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val goldSword = ItemStack(Material.GOLDEN_SWORD)
            it.inventory.setItemInMainHand(goldSword)
        }
    }

}