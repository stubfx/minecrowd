package com.stubfx.plugin.chatreactor.commands

import com.stubfx.plugin.Main
import org.bukkit.entity.EntityType

class Spawn(main: Main, private val options: String?) : Command(main) {

    override fun name(): String {
        return "spawn"
    }

    override fun behavior() {
        // this is what the function is supposed to do.
        val blacklist = listOf(EntityType.WITHER, EntityType.ENDER_DRAGON)
        EntityType.GLOW_SQUID
        var mobToSpawn = EntityType.CREEPER
        try {
            mobToSpawn = EntityType.valueOf(options?.uppercase() ?: "CREEPER")
        } catch (_: Exception) {
        }
        if (blacklist.contains(mobToSpawn)) {
            return
        }
        forEachPlayer {
            it.world.spawnEntity(it.location, mobToSpawn)
        }
    }

}