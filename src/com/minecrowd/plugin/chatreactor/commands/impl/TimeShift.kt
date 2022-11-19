package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material
import org.bukkit.entity.Chicken
import org.bukkit.entity.EntityType
import org.bukkit.entity.Zombie
import org.bukkit.inventory.ItemStack

object TimeShift : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forRandomPlayer {
            it.world.time += 12000 // add twelve mins to the time
        }
    }

}