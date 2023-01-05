package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.attribute.Attribute

object Superman : Command() {

    override val defaultCoolDown: Long = 300 * 1000
    override val cost: Int = 400

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forRandomPlayer {
            it.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 200.0
        }
        // let's fix it.
        Heal.forceRun(isSilent = true)
    }

}