package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.BlockReplacer
import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.Material

class ClearChunk(main: Main) : Command(main) {

    override fun commandName(): CommandType = CommandType.NOCHUNKNOPARTY

    override fun defaultCoolDown(): Long {
        return 600 * 1000 // 10 secs coolDown
    }

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            val chunk = it.getTargetBlockExact(100)?.chunk ?: return@forEachPlayer
            BlockReplacer.chunkReplace(main, chunk, Material.AIR)
        }
    }

}