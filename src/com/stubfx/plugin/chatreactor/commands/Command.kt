package com.stubfx.plugin.chatreactor.commands

import com.stubfx.plugin.Main
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import kotlin.random.Random


abstract class Command(mainRef: Main, val playerName: String) {

    var coolDown : Int = 0
    var ticks = mainRef.getTicks()

    init {
        main = mainRef
    }

    companion object {

        private var currentTask: BukkitTask? = null
        lateinit var main: Main
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

    fun getCloseLocationFromPlayer(location: Location): Location {
        val x = Random.nextDouble(-10.0, 10.0)
        val y = Random.nextDouble(1.0, 10.0)
        val z = Random.nextDouble(-10.0, 10.0)
        return location.add(x, y, z)
    }

    abstract fun name(): String

    abstract fun behavior()

    fun silentRun() {
        behavior()
        // no alerts here.
        // showTitle()
    }

    private fun showTitle() {
        forEachPlayer {
            it.sendTitle(title(), playerName, 10, 70, 20) // ints are def values
        }
    }

    open fun title() : String = name()

    open fun run() {
        behavior()
        showTitle()
    }

}