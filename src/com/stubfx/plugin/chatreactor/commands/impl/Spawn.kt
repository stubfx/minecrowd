package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandResultWrapper
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.entity.EntityType

object Spawn : Command() {

    override fun commandType(): CommandType = CommandType.SPAWN
    lateinit var mobToSpawn : EntityType
    private val blacklist = listOf(EntityType.WITHER, EntityType.ENDER_DRAGON, EntityType.ENDER_CRYSTAL)

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
            return resultWrapper(false, "@$playerName, this entity is in a blacklist.")
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