package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import org.bukkit.Particle
import org.bukkit.Sound

class PanicSound(main: Main, playerName: String) : Command(main, playerName) {

    override fun name(): String {
        return "Panic"
    }

    override fun behavior() {
        val sounds: List<Sound> = listOf(
            Sound.ENTITY_CREEPER_PRIMED,
            Sound.ENTITY_ENDERMAN_SCREAM,
            Sound.ENTITY_SILVERFISH_AMBIENT,
            Sound.BLOCK_END_PORTAL_SPAWN,
            Sound.ENTITY_WITHER_SHOOT,
            Sound.ENTITY_GHAST_WARN
        )
        forEachPlayer {
            it.world.spawnParticle(Particle.EXPLOSION_NORMAL, it.location, 3)
            it.world.playSound(it.location, sounds.random(), 3f, 1f)
        }
    }

}