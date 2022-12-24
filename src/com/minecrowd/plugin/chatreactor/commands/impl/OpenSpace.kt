package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.BlockReplacer
import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

object OpenSpace : Command() {


    override val defaultCoolDown: Long = 500 * 1000

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.clearAllDroppedItems()
        CommandRunner.forRandomPlayer {
            val loc1 = it.location.subtract(50.0, 0.0, 50.0)
            val loc2 = it.location.add(50.0, 50.0, 50.0)
            BlockReplacer.replaceAreaAsync(loc1, loc2, Material.AIR) {
                CommandRunner.clearAllDroppedItems()
            }
        }
    }

}