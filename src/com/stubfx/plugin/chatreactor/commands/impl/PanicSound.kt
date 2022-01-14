package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandResultWrapper
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.Particle
import org.bukkit.Sound

object PanicSound : Command() {

    override fun commandName(): CommandType = CommandType.PANIC

    override fun behavior(playerName: String, options: String?) {
        val sounds: List<Sound> = listOf(
            Sound.ENTITY_CREEPER_PRIMED,
            Sound.ENTITY_ENDERMAN_SCREAM,
            Sound.ENTITY_SILVERFISH_AMBIENT,
            Sound.BLOCK_END_PORTAL_SPAWN,
            Sound.ENTITY_WITHER_SHOOT,
            Sound.ENTITY_GHAST_WARN
        )
        CommandRunner.forEachPlayer {
            it.world.spawnParticle(Particle.EXPLOSION_NORMAL, it.location, 3)
            it.world.playSound(it.location, sounds.random(), 3f, 1f)
        }
    }

    override fun run(playerName: String, options: String?, isSilent: Boolean): CommandResultWrapper {
        return super.run(playerName, options, isSilent = true)
    }

}