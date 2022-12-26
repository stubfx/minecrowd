package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

object LaserView : Command() {


    override val defaultCoolDown: Long = 1000 * 600 // 10 mins
    override val cost: Int = 200

    override fun behavior(playerName: String, options: String?) {
        CommandRunner.startRecurrentTask {
            CommandRunner.forEachPlayer {
                it.getTargetBlockExact(20)?.type = Material.AIR
            }
        }
    }

}