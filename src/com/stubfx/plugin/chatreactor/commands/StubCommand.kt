package com.stubfx.plugin.chatreactor.commands

import com.stubfx.plugin.Main

class StubCommand(mainRef: Main, playerName: String) : Command(mainRef) {
    override fun commandName(): CommandType = CommandType.STUB

    override fun behavior(playerName: String, options: String?) {
        // congrats, do nothing.
    }

    override fun run(playerName: String): CommandResultWrapper {
        return CommandResultWrapper(commandName(), false, "wrong command.")
    }

}