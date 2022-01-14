package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack

object Diamonds : Command() {

    override fun commandType(): CommandType = CommandType.DIAMONDS

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val itemDropped: Item = it.world.dropItemNaturally(it.location, ItemStack(Material.DIAMOND, 2))
            itemDropped.pickupDelay = 40
        }
    }

}