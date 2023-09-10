package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.BlockReplacer
import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

object SuperSponge : Command() {


    override val defaultCoolDown: Long = 120 * 1000
    override val cost: Int = 400

    override fun behavior(playerName: String, options: String?) {
        // clear all the items to avoid lag
        CommandRunner.clearAllDroppedItems()
        CommandRunner.forRandomPlayer {
            val loc1 = it.location.subtract(70.0, 70.0, 70.0)
            val loc2 = it.location.add(70.0, 70.0, 70.0)
            val toChange = mutableListOf(Material.WATER, Material.LAVA, Material.KELP_PLANT, Material.KELP, Material.SEAGRASS, Material.TALL_SEAGRASS)
            BlockReplacer.replaceAreaAsync(loc1, loc2, Material.AIR, null, toChange) {
                CommandRunner.clearAllDroppedItems()
            }
        }
    }

}