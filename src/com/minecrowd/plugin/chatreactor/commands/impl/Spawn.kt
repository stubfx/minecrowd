package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandResultWrapper
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.entity.EntityType

object Spawn : Command() {


    private var mobToSpawn: EntityType = EntityType.CREEPER
    private val blacklist =
        listOf(EntityType.WITHER, EntityType.ENDER_DRAGON, EntityType.ENDER_CRYSTAL, EntityType.WARDEN, EntityType.PLAYER, EntityType.UNKNOWN)

    override fun tabCompleterOptions(): List<String> {
        return EntityType.values().map { it.toString() }
    }

    override fun setup(playerName: String, options: String?): CommandResultWrapper {
        // this is what the function is supposed to do.
        try {
            mobToSpawn = EntityType.valueOf(options?.uppercase() ?: "")
        } catch (_: Exception) {
            // not found?
            return resultWrapper(false, "@$playerName, entity not found.")
        }
        if (blacklist.contains(mobToSpawn)) {
            return resultWrapper(false, "@$playerName, this entity cannot be spawned.")
        }
        return super.setup(playerName, options)
    }

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val entity = it.world.spawnEntity(it.location, mobToSpawn)
            entity.customName = playerName
            entity.isCustomNameVisible = true
        }
    }

}