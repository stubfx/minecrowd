package com.minecrowd.plugin.chatreactor

import com.minecrowd.plugin.Main
import com.minecrowd.plugin.chatreactor.commands.CommandRunner
import org.bukkit.ChatColor
import org.bukkit.Material

object PointsManager {
    lateinit var main: Main
    private var currentAmount = 0
    private var addPointsEvery = 60 // secs
    private var pointsToAdd = 10
    private var lastBroadcastedAmount = 0
    private var valuePerItem = 2
    private var minPointCap = -20

    fun setMainRef(mainRef: Main) {
        main = mainRef
    }

    fun start() {
        startClock()
        startInventoryChecker()
    }

    private fun startInventoryChecker() {
        CommandRunner.startPersistentTask({
            var materialAmount = 0
            // check if the player has the block in his inventory
            CommandRunner.forEachPlayer {
                for (materialInInventory in it.inventory.all(Material.WOODEN_SHOVEL)) {
                    materialAmount += materialInInventory.value.amount
                    it.inventory.clear(materialInInventory.key)
                }
            }
            if (materialAmount > 0) changeScore(-materialAmount * valuePerItem)
        })
    }

    private fun startClock() {
        CommandRunner.startPersistentTask({
            changeScore(pointsToAdd)
        }, addPointsEvery * 20L)
    }

    fun changeScore(amount: Int){
        currentAmount += amount
        // cap at -100
        if(currentAmount < minPointCap) currentAmount = minPointCap
        broadcastPoints()
    }

    private fun broadcastPoints(){
        var msg = if (lastBroadcastedAmount < currentAmount) {
            // in this case points should be red
            "Points : &c$currentAmount"
        } else {
            // in this other case they should be green
            "Points : &a$currentAmount"
        }
        lastBroadcastedAmount = currentAmount
        msg = ChatColor.translateAlternateColorCodes('&',msg)
        main.server.broadcastMessage(msg)
    }

}