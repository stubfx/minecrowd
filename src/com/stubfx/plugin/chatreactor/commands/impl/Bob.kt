package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.Material
import org.bukkit.entity.Chicken
import org.bukkit.entity.EntityType
import org.bukkit.entity.Zombie
import org.bukkit.inventory.ItemStack

class Bob(main: Main) : Command(main) {

    override fun commandName(): CommandType = CommandType.BOB

    override fun behavior(playerName: String, options: String?) {
        forEachPlayer {
            val zombie = it.world.spawnEntity(it.location, EntityType.ZOMBIE) as Zombie
            val chicken = it.world.spawnEntity(it.location, EntityType.CHICKEN) as Chicken
            zombie.setBaby()
            zombie.customName = "bob"
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