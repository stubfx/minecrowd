package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.entity.Creeper
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity

object CreepyLand : Command() {


    override val defaultCoolDown: Long = 180 * 1000
    override val cost: Int = 400

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forRandomPlayer { player ->
            player.getNearbyEntities(100.0, 100.0, 100.0).forEach {
                val creeper = it.world.spawnEntity(it.location, EntityType.CREEPER) as Creeper
                creeper.isPowered = true
                if (it is LivingEntity) {
                    it.health = 0.0
                }
            }
        }
        // clean it up, just to make sure.
        CommandRunner.clearAllDroppedItems()
    }

}