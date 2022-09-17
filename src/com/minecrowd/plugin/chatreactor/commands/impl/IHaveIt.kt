package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.inventory.ItemStack

object IHaveIt : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.startRecurrentTask {
            CommandRunner.forEachPlayer {
                val type = it.getTargetBlockExact(100)?.type ?: return@forEachPlayer
                it.inventory.addItem(ItemStack(type))
            }
        }
    }

}