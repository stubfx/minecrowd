package com.stubfx.plugin.chatreactor.commands

import com.stubfx.plugin.Main
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

object CommandRunner {

    private var currentTask: BukkitTask? = null
    lateinit var main: Main
    private var currentTaskTickCount = 0

    fun runOnBukkit(func: () -> Unit): BukkitTask {
        return object : BukkitRunnable() {
            override fun run() {
                func()
            }
        }.runTask(main)
    }

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

    private fun stopTask() {
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

    fun setMainRef(mainRef: Main) {
        main = mainRef
    }

}