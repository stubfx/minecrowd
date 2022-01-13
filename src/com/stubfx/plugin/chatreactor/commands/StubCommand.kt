package com.stubfx.plugin.chatreactor.commands

import com.stubfx.plugin.Main

class StubCommand(mainRef: Main, playerName: String) : Command(mainRef, playerName) {
    override fun name(): String = ""

    override fun behavior() {
        // congrats, do nothing.
    }

    override fun run(): CommandResultWrapper {
        return CommandResultWrapper("stub", false, "wrong command.")
    }

}