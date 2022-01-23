package com.stubfx.plugin.chatreactor.commands.impl

import com.stubfx.plugin.chatreactor.commands.Command
import com.stubfx.plugin.chatreactor.commands.CommandRunner
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.potion.PotionEffectType

object Potion : Command() {

    private val potions = listOf(*PotionEffectType.values())
    private lateinit var selectedPotion: PotionEffectType

    override fun commandType(): CommandType = CommandType.POTION

    override fun title(): String = selectedPotion.toString().replace("_", " ")

    override fun behavior(playerName: String, options: String?) {
        selectedPotion = potions.random()
        CommandRunner.forEachPlayer {
            val levitation = selectedPotion.createEffect(ticks * 5, 3)
            it.addPotionEffect(levitation)
        }
    }
}