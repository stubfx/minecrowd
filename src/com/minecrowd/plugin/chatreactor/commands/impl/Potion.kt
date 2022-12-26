package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandResultWrapper
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.potion.PotionEffectType

object Potion : Command() {

    private val potions = listOf(*PotionEffectType.values())
    private lateinit var selectedPotion: PotionEffectType
    // this is here just to avoid lateinit exeptions
    // cause we are running on a separate thread, that means that the title


    override val defaultCoolDown: Long = 1000 * 60
    override val cost: Int = 80

    override fun title(): String = selectedPotion.name.replace("_", " ")

    override fun setup(playerName: String, options: String?): CommandResultWrapper {
        selectedPotion = potions.random()
        return super.setup(playerName, options)
    }

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.forEachPlayer {
            val levitation = selectedPotion.createEffect(ticks * 60, 3)
            it.addPotionEffect(levitation)
        }
    }
}