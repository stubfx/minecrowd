package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object Armored : Command() {

    override fun commandName(): CommandType = CommandType.ARMORED

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            it.inventory.helmet = ItemStack(Material.NETHERITE_HELMET)
            it.inventory.chestplate = ItemStack(Material.NETHERITE_CHESTPLATE)
            it.inventory.leggings = ItemStack(Material.NETHERITE_LEGGINGS)
            it.inventory.boots = ItemStack(Material.NETHERITE_BOOTS)
            it.inventory.setItemInMainHand(ItemStack(Material.NETHERITE_SWORD))
        }
    }

}