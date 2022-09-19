package com.minecrowd.plugin.chatreactor.commands.impl.teleport

import com.minecrowd.plugin.PluginUtils
import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner

object ToTheNether : Command() {

    override fun defaultCoolDown(): Long {
        return 600 * 1000
    }

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            PluginUtils.teleportToNether(it)
        }
    }

}