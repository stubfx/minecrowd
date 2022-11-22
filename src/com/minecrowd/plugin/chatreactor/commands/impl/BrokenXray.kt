package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.BlockReplacer
import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

object BrokenXray : Command() {

    override fun defaultCoolDown(): Long {
        return 200 * 1000
    }

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val distance = 30.0
            val loc1 = it.location.clone().subtract(distance, 0.0, distance)
            loc1.y = -63.0
            val loc2 = it.location.clone().add(distance, 0.0, distance)
            loc2.y = 255.0
            BlockReplacer.forEachBlockAsync(loc1, loc2, { block ->
                if (block.type != Material.IRON_ORE &&
                        block.type != Material.COPPER_ORE &&
                        block.type != Material.DIAMOND_ORE &&
                        block.type != Material.REDSTONE_ORE
                        ) {
                    block.type = Material.GLASS
                }
            })
        }
        CommandRunner.clearAllDroppedItems()
    }

}