package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.entity.Item

object DropIt : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val item = it.inventory.itemInMainHand
            if (!item.type.isAir) {
                it.inventory.remove(item)
                val itemDropped: Item = it.world.dropItemNaturally(it.location, item)
                itemDropped.pickupDelay = 100
            }
        }
    }

}