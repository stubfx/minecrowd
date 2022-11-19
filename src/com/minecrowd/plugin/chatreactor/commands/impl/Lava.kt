package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

object Lava : Command() {


    override fun defaultCoolDown(): Long {
        return 1000 * 500
    }

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            it.location.block.type = Material.LAVA
        }
    }

}