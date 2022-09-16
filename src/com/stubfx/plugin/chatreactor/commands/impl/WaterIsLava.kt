package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.BlockReplacer
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

object WaterIsLava : Command() {


    override fun defaultCoolDown(): Long {
        return 600 * 1000 // 10 min coolDown
    }

    override fun behavior(playerName: String, options: String?) {
        // clear all the items to avoid lag
        CommandRunner.clearAllDroppedItems()
        CommandRunner.forRandomPlayer {
            val loc1 = it.location.subtract(70.0, 70.0, 70.0)
            val loc2 = it.location.add(70.0, 70.0, 70.0)
            val material = if (it.world.name == "world") Material.LAVA else Material.WATER
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