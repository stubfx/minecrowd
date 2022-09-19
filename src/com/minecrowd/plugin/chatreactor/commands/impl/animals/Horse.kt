package com.minecrowd.plugin.chatreactor.commands.impl.animals

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

object Horse : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val skeletonHorse = it.world.spawnEntity(it.location, EntityType.SKELETON_HORSE) as org.bukkit.entity.SkeletonHorse
            skeletonHorse.owner = it
            skeletonHorse.inventory.saddle = ItemStack(Material.SADDLE, 1)
        }
    }

}