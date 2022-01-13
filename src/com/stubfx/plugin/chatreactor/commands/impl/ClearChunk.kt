package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.BlockReplacer
import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import org.bukkit.Material

class ClearChunk(main: Main, playerName: String) : Command(main, playerName) {

    override fun name(): String {
        return "clearchunk"
    }

    override fun behavior() {
        forEachPlayer {
            val chunk = it.getTargetBlockExact(100)?.chunk ?: return@forEachPlayer
            BlockReplacer.chunkReplace(main, chunk, Material.AIR)
        }
    }

}