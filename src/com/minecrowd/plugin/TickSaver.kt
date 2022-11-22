package com.minecrowd.plugin

import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import java.util.*

object TickSaver {
    // WIP
    private lateinit var main: Main
    private var lastTickEpoch = Date().time

    fun start(mainRef: Main) {
        main = mainRef
        CommandRunner.startPersistentTask({
            val timeDiff = Date().time - lastTickEpoch
            if (timeDiff > 100) PluginUtils.broadcastMessage("This tick took ${timeDiff}ms.")
//            CommandRunner.clearAllDroppedItems()
            lastTickEpoch = Date().time
        }, 1L)
    }

}