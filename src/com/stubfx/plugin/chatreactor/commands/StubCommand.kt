package com.stubfx.plugin.chatreactor.commands

object StubCommand : Command() {
    override fun commandType(): CommandType = CommandType.STUB

    override fun behavior(playerName: String, options: String?) {
        // congrats, do nothing.
    }

    override fun run(playerName: String, options: String?, isSilent: Boolean?): CommandResultWrapper {
        return CommandResultWrapper(commandType(), false, "Wrong Command")
    }

}