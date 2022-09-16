package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.BlockReplacer
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

object ClearChunk : Command() {


    override fun defaultCoolDown(): Long {
        return 600 * 1000 // 10 min coolDown
    }

    override fun behavior(playerName: String, options: String?) {
        // clear all the items to avoid lag
        CommandRunner.clearAllDroppedItems()
        CommandRunner.forRandomPlayer {
            val chunk = it.location.chunk
            BlockReplacer.chunkReplace(chunk, Material.AIR) {
                CommandRunner.clearAllDroppedItems()
            }
        }
    }

}