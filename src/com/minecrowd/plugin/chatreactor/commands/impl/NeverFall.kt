package com.minecrowd.plugin.chatreactor.commands.impl

import com.minecrowd.plugin.chatreactor.commands.Command
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Material

object NeverFall : Command() {


    override fun behavior(playerName: String, options: String?) {
        CommandRunner.startRecurrentTask {
            CommandRunner.forEachPlayer {
                it.location.subtract(0.0, 1.0, 0.0).block.type = Material.ORANGE_WOOL
            }
        }
    }

}