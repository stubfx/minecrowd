package com.minecrowd.plugin.chatreactor.commands

import com.minecrowd.plugin.Main
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask


object CommandRunner {

    private var currentTask: BukkitTask? = null
    private var currentTaskTickCount = 0
    lateinit var main: Main

    fun setMainRef(mainRef: Main) {
        main = mainRef
    }

    fun forEachPlayer(func: (player: Player) -> Unit) {
        getRealPlayers().forEach { func(it) }
    }

    fun forRandomPlayer(func: (player: Player) -> Unit) {
        if (getRealPlayers().isNotEmpty()) {
            func(getRealPlayers().random())
        }
    }

    fun clearAllDroppedItems() {
        main.server.worlds.forEach { world ->
            world.entities.forEach {
                if (it is Item) {
                    it.remove()
                }
            }
        }
    }

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

    fun startPersistentTask(func: () -> Unit, tickPeriod: Long = 1L): BukkitTask {
        return object : BukkitRunnable() {
            override fun run() {
                func()
            }
        }.runTaskTimer(main, 1, tickPeriod)
    }

    /**
     * returns the list of effective players excluding OPs
     */
    fun getRealPlayers(): List<Player> {
        return main.server.onlinePlayers.filter { !it.isOp }
//        return main.server.onlinePlayers.toList()
    }

}