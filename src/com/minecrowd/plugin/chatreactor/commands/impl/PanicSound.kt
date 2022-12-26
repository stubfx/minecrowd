package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandResultWrapper
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Particle
import org.bukkit.Sound

object PanicSound : Command() {

    override val defaultCoolDown: Long = 10 * 1000
    override val cost: Int = 30

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
            it.world.playSound(it.location, sounds.random(), 10f, 1f)
        }
    }

    override fun run(playerName: String, options: String?, isSilent: Boolean): CommandResultWrapper {
        return super.run(playerName, options, isSilent = true)
    }

}