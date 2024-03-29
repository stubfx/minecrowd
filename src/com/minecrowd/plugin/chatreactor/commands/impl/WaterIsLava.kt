package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.BlockReplacer
import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

object WaterIsLava : Command() {


    override val defaultCoolDown: Long = 600 * 1000
    override val cost: Int = 1000

    override fun behavior(playerName: String, options: String?) {
        // clear all the items to avoid lag
        CommandRunner.clearAllDroppedItems()
        CommandRunner.forRandomPlayer {
            val loc1 = it.location.subtract(70.0, 70.0, 70.0)
            val loc2 = it.location.add(70.0, 70.0, 70.0)
            // gotta check with the nether cause the player may be in a different world created by a player.
            val material = if (it.world.name == "world_nether") Material.WATER else Material.LAVA
            val toChange = if (material == Material.WATER) Material.LAVA else Material.WATER
            println(material)
            println(toChange)
            BlockReplacer.replaceAreaAsync(loc1, loc2, Material.BARRIER, null, mutableListOf(toChange)) {
                CommandRunner.clearAllDroppedItems()
            }
            BlockReplacer.replaceAreaAsync(loc1, loc2, material, null, mutableListOf(Material.BARRIER)) {
                CommandRunner.clearAllDroppedItems()
            }
        }
    }

}