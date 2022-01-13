package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import org.bukkit.TreeType

class TreeCage(main: Main, playerName: String) : Command(main, playerName) {

    override fun name(): String {
        return "treecage"
    }

    override fun behavior() {
        forEachPlayer {
            it.world.generateTree(it.location.add(1.toDouble(), 0.toDouble(), 0.toDouble()), TreeType.BIG_TREE)
            it.world.generateTree(it.location.add((-1).toDouble(), 0.toDouble(), 0.toDouble()), TreeType.BIG_TREE)
            it.world.generateTree(it.location.add(0.toDouble(), 0.toDouble(), 1.toDouble()), TreeType.BIG_TREE)
            it.world.generateTree(it.location.add(0.toDouble(), 0.toDouble(), (-1).toDouble()), TreeType.BIG_TREE)
        }
    }

}