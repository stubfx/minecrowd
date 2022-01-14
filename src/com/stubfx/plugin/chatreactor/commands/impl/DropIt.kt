package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import org.bukkit.entity.Item

class DropIt(main: Main) : Command(main) {

    override fun commandName(): String {
        return "DropIt"
    }

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            val item = it.inventory.itemInMainHand
            if (!item.type.isAir) {
                it.inventory.remove(item)
                val itemDropped: Item = it.world.dropItemNaturally(it.location, item)
                itemDropped.pickupDelay = 100
            }
        }
    }

}