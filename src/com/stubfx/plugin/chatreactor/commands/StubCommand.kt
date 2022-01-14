package com.stubfx.plugin.chatreactor.commands

import com.stubfx.plugin.Main

object StubCommand : Command() {
    override fun commandName(): CommandType = CommandType.STUB

    override fun behavior(playerName: String, options: String?) {
        // congrats, do nothing.
    }

    override fun run(playerName: String, options: String?, isSilent: Boolean): CommandResultWrapper {
        return CommandResultWrapper(commandName(), false, "wrong command.")
    }

}