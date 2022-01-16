package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

object Cookies : Command() {

    override fun commandType(): CommandType = CommandType.COOKIES

    override fun behavior(playerName: String, options: String?) {
        val cookiesCount = Random.nextInt(32, 128)
        CommandRunner.forEachPlayer {
            val itemDropped: Item = it.world.dropItemNaturally(it.location, ItemStack(Material.COOKIE, cookiesCount))
            itemDropped.pickupDelay = 40
        }
    }

}