package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import org.bukkit.inventory.ItemStack

class IHaveIt(main: Main, playerName: String) : Command(main, playerName) {

    override fun name(): String {
        return "ihaveit"
    }

    override fun behavior() {
        CommandRunner.startRecurrentTask {
            forEachPlayer {
                val type = it.getTargetBlockExact(100)?.type ?: return@forEachPlayer
                it.inventory.addItem(ItemStack(type))
            }
        }
    }

}