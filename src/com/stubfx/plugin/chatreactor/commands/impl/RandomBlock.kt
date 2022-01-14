package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack

class RandomBlock(main: Main) : Command(main) {

    override fun commandName(): CommandType = CommandType.RANDOMBLOCK

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            val itemDropped: Item =
                it.world.dropItemNaturally(it.location, ItemStack(listOf(*Material.values()).random(), 1))
            itemDropped.pickupDelay = 40
        }
    }

}