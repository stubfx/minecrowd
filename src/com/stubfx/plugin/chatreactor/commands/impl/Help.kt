package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command

object Help : Command() {

    override val showSuccessMessage: Boolean
        get() = true

    override fun defaultCoolDown(): Long {
        return 10 * 1000 // 10 sec
    }

    override fun successMessage(): String {
        return "You can find the list of commands here: https://github.com/stubfx/minecraft_plugin"
    }

    override fun behavior(playerName: String, options: String?) {

    }

}