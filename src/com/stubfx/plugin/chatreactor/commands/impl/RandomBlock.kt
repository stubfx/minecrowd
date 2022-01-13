package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack

class RandomBlock(main: Main, playerName: String) : Command(main, playerName) {

    override fun name(): String {
        return "randomblock"
    }

    override fun behavior() {
        forEachPlayer {
            val itemDropped: Item =
                it.world.dropItemNaturally(it.location, ItemStack(listOf(*Material.values()).random(), 1))
            itemDropped.pickupDelay = 40
        }
    }

}