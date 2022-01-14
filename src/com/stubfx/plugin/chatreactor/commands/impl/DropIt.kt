package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.entity.Item

object DropIt : Command() {

    override fun commandName(): CommandType = CommandType.DROPIT

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