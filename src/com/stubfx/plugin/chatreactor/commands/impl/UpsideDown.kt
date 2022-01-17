package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.BlockReplacer
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.Location
import org.bukkit.Material

object UpsideDown : Command() {

    override fun commandType(): CommandType = CommandType.UPSIDEDOWN

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val distance = 20.0
            val offset = 10.0
            val loc1 = it.location.subtract(distance, offset, distance)
            val loc2 = it.location.add(distance, 20.0 - (offset + 1) , distance)
            val targetHeight = it.location.y + 30
            BlockReplacer.forEachBlock(loc1, loc2) { block ->
                val loc = block.location.clone()
                loc.y = targetHeight - ((block.y - loc1.y))
                loc.block.type = block.type
            }
        }
    }

}