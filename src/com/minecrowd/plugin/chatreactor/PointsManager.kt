package com.minecrowd.plugin.chatreactor

import com.minecrowd.plugin.ConfigManager
import com.minecrowd.plugin.Main
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard

object BlocksToFind {
    var blocks = listOf<Material>(
        Material.WOODEN_SHOVEL,
        Material.SUGAR_CANE,
        Material.COBBLESTONE,
        Material.NETHERRACK,
        Material.OBSIDIAN,
        Material.DIAMOND,
        Material.MILK_BUCKET,
        Material.WHEAT,
        Material.IRON_INGOT,
        Material.COAL,
        Material.WATER_BUCKET,
        Material.LAVA_BUCKET,
        Material.SAND,
        Material.ROTTEN_FLESH,
        Material.BONE,
        Material.LEATHER,
        Material.CHICKEN,
        Material.POPPY,
        Material.DANDELION,
        Material.CORNFLOWER,
        Material.PUMPKIN,
        Material.CAKE,
        Material.HAY_BLOCK,
        Material.BOOKSHELF,
        Material.ENCHANTING_TABLE,
        Material.ENDER_PEARL,
        Material.TNT,
        Material.DIAMOND_ORE
    )
}

object PointsManager {
    lateinit var main: Main
    private var currentAmount = 0
    private var addPointsEvery = 3 // secs
    private var pointsToAdd = 30
    private var playerPointsMultiplayer = 1
    private var minPointCap = -20

    private lateinit var board: Scoreboard
    private lateinit var objective: Objective
    private lateinit var currentRndBlock: Material
    private var enabled = true

    fun setMainRef(mainRef: Main) {
        main = mainRef
    }

    fun start() {
        this.enabled = ConfigManager.isPointManagerEnabled()
        if (enabled) {
            startClock()
            initScoreboard()
            startStats()
            startInventoryChecker()
        }
    }

    private fun initScoreboard() {
        val scoreboardManager = Bukkit.getScoreboardManager()
        board = scoreboardManager?.newScoreboard!!
        objective = board.registerNewObjective("Minecrowd_scoreboard", Criteria.DUMMY, "EMPTY")
        objective.displaySlot = DisplaySlot.SIDEBAR
    }

    private fun startStats() {
        CommandRunner.startPersistentTask({
            CommandRunner.forEachPlayer {
                //objective.getScore("Points: ").score = currentAmount
                it.scoreboard = board
            }
        })
    }

    fun getCurrentAmount(): Int = currentAmount

    fun checkEnoughPoints(points: Int): Boolean {
        return if (!this.enabled) {
            // if pointsManager is not enabled, always return true.
            true
        } else {
            // check otherwise!
            (getCurrentAmount() - points) > 0
        }
    }

    private fun generateRandomBlock() {
        currentRndBlock = BlocksToFind.blocks.random()
        objective.displayName = "Block: ${currentRndBlock.name}"
    }

    private fun startInventoryChecker() {
        // start by generating the block to look for
        generateRandomBlock()
        // checks if the player has found the given material on every tick
        // if the material has been found, let's generate a new one to look for.
        CommandRunner.startPersistentTask({
            // check if the player has the block in his inventory
            var found = false
            CommandRunner.forEachPlayer {
                for (materialInInventory in it.inventory.all(currentRndBlock)) {
                    it.inventory.clear(materialInInventory.key)
                    found = true
                }
            }
            if (found) {
                CommandRunner.forEachPlayer {
                    it.sendTitle("Block has been found!", currentRndBlock.name, 10, 70, 20) // ints are def values
                }
                // someone has found the given block.
                resetPoints()
                generateRandomBlock()
            }
        })
    }

    private fun resetPoints() {
        changeScore(-currentAmount)
    }

    private fun startClock() {
        CommandRunner.startPersistentTask({
            changeScore(pointsToAdd + CommandRunner.getRealPlayers().size * playerPointsMultiplayer)
        }, addPointsEvery * 20L)
    }

    fun changeScore(amount: Int) {
        if (!this.enabled) {
            return
        }
        currentAmount += amount
        // cap at -100
        if (currentAmount < minPointCap) currentAmount = minPointCap
        broadcastPoints()
    }

    private fun broadcastPoints() {
        objective.getScore("Points: ").score = currentAmount
    }

}