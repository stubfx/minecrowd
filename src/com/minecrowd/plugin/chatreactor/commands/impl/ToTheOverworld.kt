package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.PluginUtils
import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner

object ToTheOverworld : Command() {


    override fun defaultCoolDown(): Long {
        return 600 * 1000
    }

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            PluginUtils.teleportToOverworld(it)
        }
    }

}