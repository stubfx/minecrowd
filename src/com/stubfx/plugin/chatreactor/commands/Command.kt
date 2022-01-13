package com.stubfx.plugin.chatreactor.commands

import com.stubfx.plugin.Main
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask


abstract class Command(mainRef: Main) {

    init {
        main = mainRef
    }

    companion object {

        private var currentTask: BukkitTask? = null
        private lateinit var main: Main
        private var currentTaskTickCount = 0

        private fun runOnBukkitEveryTick(func: () -> Unit, duration: Int): BukkitTask {
            val taskDuration = main.getTicks() * duration
            return object : BukkitRunnable() {
                override fun run() {
                    currentTaskTickCount++
                    func()
                    if (currentTaskTickCount > taskDuration) {
                        this.cancel()
                    }
                }
            }.runTaskTimer(main, 1, 1)
        }

        fun stopTask() {
            currentTask?.cancel()
        }

        fun startShortRecurrentTask(func: () -> Unit) {
            stopTask()
            currentTaskTickCount = 0
            currentTask = runOnBukkitEveryTick(func, 1)
        }

        fun startRecurrentTask(func: () -> Unit) {
            stopTask()
            currentTaskTickCount = 0
            currentTask = runOnBukkitEveryTick(func, 20)
        }

    }

    fun forEachPlayer(func: (player: Player) -> Unit) {
        main.server.onlinePlayers.forEach { func(it) }
    }

    var coolDown : Int = 0

    abstract fun name(): String

    abstract fun behavior()

    fun run() {
        behavior()
    }

}