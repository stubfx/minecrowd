package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.TreeType

object TreeCage : Command() {

    override fun commandName(): CommandType = CommandType.TREE

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            it.world.generateTree(it.location.add(1.toDouble(), 0.toDouble(), 0.toDouble()), TreeType.BIG_TREE)
            it.world.generateTree(it.location.add((-1).toDouble(), 0.toDouble(), 0.toDouble()), TreeType.BIG_TREE)
            it.world.generateTree(it.location.add(0.toDouble(), 0.toDouble(), 1.toDouble()), TreeType.BIG_TREE)
            it.world.generateTree(it.location.add(0.toDouble(), 0.toDouble(), (-1).toDouble()), TreeType.BIG_TREE)
        }
    }

}