package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.BlockReplacer
import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

object Parkour : Command() {

    override val defaultCoolDown: Long = 200 * 1000
    override val cost: Int = 200

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forRandomPlayer {
            val distance = 20.0
            val offset = 20.0
            val loc1 = it.location.subtract(distance, offset, distance)
            val loc2 = it.location.add(distance, 20.0, distance)
//            val targetHeight = it.location.y + 30
            BlockReplacer.forEachBlockAsync(loc1, loc2, { block ->
                if (block.x % 2 == 0 && block.z % 2 == 0) {
                    block.type = Material.AIR
                }
            })
        }
        CommandRunner.clearAllDroppedItems()
    }

}