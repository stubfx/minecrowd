package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner

object Clear : Command() {

    override val defaultCoolDown: Long = 800 * 1000
    override val cost: Int = 1200


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forRandomPlayer {
            it.inventory.clear()
        }
    }

}