package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

object Paint : Command() {


    override fun behavior(playerName: String, options: String?) {
        val wool: Material = listOf(
            Material.WHITE_WOOL,
            Material.BLUE_WOOL,
            Material.RED_WOOL,
            Material.CYAN_WOOL,
            Material.GRAY_WOOL,
        ).random()
        CommandRunner.startRecurrentTask {
            CommandRunner.forEachPlayer {
                it.getTargetBlockExact(100)?.type = wool
            }
        }
    }

}