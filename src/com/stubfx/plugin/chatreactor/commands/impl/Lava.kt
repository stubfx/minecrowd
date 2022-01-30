package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.Material

object Lava : Command() {

    override fun commandType(): CommandType = CommandType.LAVA

    override fun defaultCoolDown(): Long {
        return 1000 * 600 // 10 mins
    }

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            it.location.block.type = Material.LAVA
        }
    }

}