package com.minecrowd.plugin.chatreactor.commands.impl.teleport

import com.minecrowd.plugin.PluginUtils
import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner

object ToTheEnd : Command() {

    override val defaultCoolDown: Long = 600 * 1000
    override val cost: Int = 1200

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            PluginUtils.teleportToEnd(it)
        }
    }

}