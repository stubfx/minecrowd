package com.minecrowd.plugin.chatreactor.commands.impl.animals

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.entity.EntityType

object Cat : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val cat = it.world.spawnEntity(it.location, EntityType.CAT) as org.bukkit.entity.Cat
            cat.owner = it
        }
    }

}