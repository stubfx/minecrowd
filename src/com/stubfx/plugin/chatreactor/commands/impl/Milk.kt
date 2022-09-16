package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object Milk : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            it.inventory.setItemInMainHand(ItemStack(Material.MILK_BUCKET))
        }
    }

}