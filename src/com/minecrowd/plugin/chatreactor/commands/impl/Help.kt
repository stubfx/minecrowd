package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandResultWrapper

object Help : Command() {

    override val showSuccessMessage: Boolean
        get() = true

    override fun defaultCoolDown(): Long {
        return 10 * 1000 // 10 sec
    }

    override fun successMessage(): String {
        return "You can find the list of commands here: https://minecrowd.stubfx.com/"
    }

    override fun behavior(playerName: String, options: String?) {

    }

    override fun run(playerName: String, options: String?, isSilent: Boolean): CommandResultWrapper {
        return super.run(playerName, options, isSilent = true)
    }

}