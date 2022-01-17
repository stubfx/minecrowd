package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.Material

object Anvil : Command() {

    override fun commandType(): CommandType = CommandType.ANVIL

    override fun defaultCoolDown(): Long {
        return 120 * 1000 // 2 mins
    }

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.startRecurrentTask {
            CommandRunner.forEachPlayer {
                val closeLocation = getCloseLocationFromPlayer(it.location, 10.0)
                closeLocation.block.type = Material.ANVIL
            }
        }
    }

}