package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material
import org.bukkit.entity.Chicken
import org.bukkit.entity.EntityType
import org.bukkit.entity.Zombie
import org.bukkit.inventory.ItemStack

object Bob : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val zombie = it.world.spawnEntity(it.location, EntityType.ZOMBIE) as Zombie
            val chicken = it.world.spawnEntity(it.location, EntityType.CHICKEN) as Chicken
            zombie.setBaby()
            zombie.customName = "BoB ($playerName)"
            zombie.isCustomNameVisible = true
            zombie.equipment?.helmet = ItemStack(Material.DIAMOND_HELMET)
            zombie.equipment?.chestplate = ItemStack(Material.DIAMOND_CHESTPLATE)
            zombie.equipment?.leggings = ItemStack(Material.DIAMOND_LEGGINGS)
            zombie.equipment?.boots = ItemStack(Material.DIAMOND_BOOTS)
            zombie.equipment?.setItemInMainHand(ItemStack(Material.NETHERITE_SWORD))
            chicken.addPassenger(zombie)
        }
    }

}