package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.BlockReplacer
import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

object BrokenXray : Command() {

    override val defaultCoolDown: Long = 200 * 1000
    override val cost: Int = 500

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forRandomPlayer {
            val distance = 30.0
            val loc1 = it.location.clone().subtract(distance, 0.0, distance)
            loc1.y = it.location.y - 50.0
            val loc2 = it.location.clone().add(distance, 0.0, distance)
            loc2.y = it.location.y
            BlockReplacer.forEachBlockAsync(loc1, loc2, { block ->
                if (!block.type.isAir &&
                    block.type != Material.IRON_ORE &&
                    block.type != Material.COPPER_ORE &&
                    block.type != Material.DIAMOND_ORE &&
                    block.type != Material.REDSTONE_ORE &&
                    block.type != Material.ANCIENT_DEBRIS &&
                    block.type != Material.LAVA &&
                    block.type != Material.WATER
                ) {
                    block.type = Material.GLASS
                }
            })
        }
        CommandRunner.clearAllDroppedItems()
    }

}