package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.BlockReplacer
import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner

object UpsideDown : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forRandomPlayer {
            val distance = 20.0
            val offset = 10.0
            val loc1 = it.location.subtract(distance, offset, distance)
            val loc2 = it.location.add(distance, 20.0 - (offset + 1), distance)
            val targetHeight = it.location.y + 30
            BlockReplacer.forEachBlockAsync(loc1, loc2, { block ->
                val loc = block.location.clone()
                loc.y = targetHeight - ((block.y - loc1.y))
                loc.block.type = block.type
            })
        }
        CommandRunner.clearAllDroppedItems()
    }

}