package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Sound
import org.bukkit.entity.EntityType

object ThatIsTNT : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val target = it.getTargetBlockExact(50) ?: return@forEachPlayer
            target.world.playSound(target.location, Sound.ENTITY_TNT_PRIMED, 3f, 1f)
            target.world.spawnEntity(target.location, EntityType.PRIMED_TNT)
        }
    }

}