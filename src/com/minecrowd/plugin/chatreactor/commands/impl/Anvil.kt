package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

object Anvil : Command() {


    override fun defaultCoolDown(): Long {
        return 120 * 1000 // 2 mins
    }

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.startRecurrentTask {
            CommandRunner.forEachPlayer {
                val closeLocation = getCloseLocation(it.location, 10.0)
                closeLocation.block.type = Material.ANVIL
            }
        }
    }

}