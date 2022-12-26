package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

object Anvil : Command() {


    override val defaultCoolDown: Long = 120 * 1000 // 2 mins
    override val cost: Int = 200

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.startRecurrentTask {
            CommandRunner.forRandomPlayer {
                val closeLocation = getCloseLocation(it.location, 10.0)
                closeLocation.block.type = Material.ANVIL
            }
        }
    }

}