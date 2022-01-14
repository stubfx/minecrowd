package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.inventory.ItemStack

class IHaveIt(main: Main) : Command(main) {

    override fun commandName(): CommandType = CommandType.IHAVEIT

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.startRecurrentTask {
            forEachPlayer {
                val type = it.getTargetBlockExact(100)?.type ?: return@forEachPlayer
                it.inventory.addItem(ItemStack(type))
            }
        }
    }

}