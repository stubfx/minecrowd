package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack

object RandomBlock : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val itemDropped: Item =
                it.world.dropItemNaturally(it.location, ItemStack(listOf(*Material.values()).random(), 1))
            itemDropped.pickupDelay = 40
        }
    }

}