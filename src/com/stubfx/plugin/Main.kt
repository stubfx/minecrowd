package com.stubfx.plugin

import com.stubfx.plugin.listeners.EntityListener
import com.stubfx.plugin.listeners.PlayerListener
import com.stubfx.plugin.listeners.ProjectileListener
import com.stubfx.plugin.listeners.behaviours.PlayerListenerBehaviour
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask


class Main : JavaPlugin() {

    private val DRILL_OFFSET = 2
    private var myTask: BukkitTask? = null

    private var materialReplaceMap: HashMap<Player, Pair<Location?, Location?>> = HashMap()

    override fun onEnable() {
        getCommand("clearchunk")?.tabCompleter = MaterialTabCompleter()
        getCommand("sectionreplace")?.tabCompleter = MaterialTabCompleter()
        getCommand("chunkreplace")?.tabCompleter = MaterialTabCompleter()
        server.pluginManager.registerEvents(PlayerListener(this), this)
        server.pluginManager.registerEvents(EntityListener(this), this)
        server.pluginManager.registerEvents(ProjectileListener(this), this)
    }

    fun consoleLog(msg: String) {
        server.consoleSender.sendMessage("STUB LOG: $msg")
    }

    fun getTicks(): Int {
        return 20
    }

    override fun onDisable() {
        clearTask()
    }

    private fun clearTask() {
        myTask?.cancel()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        clearTask()
        // in this case we found me.
        checkCommand(sender as Player, command, args)
        return false
    }

    private fun checkCommand(player: Player, command: Command, args: Array<String>) {
        when (command.name.lowercase()) {
            "draw" -> draw(player)
            "drill" -> drill(player)
            "clearchunk" -> clearChunk(player.location, args)
            "sculktrap" -> sculktrap(player)
            "writeas" -> writeas(args)
            "sectionreplace" -> sectionReplace(player, args)
            "chunkreplace" -> chunkreplace(player, args)
            "stubstop" -> clearTask()
        }
    }

    private fun chunkreplace(player: Player, args: Array<String>) {
        BlockReplacer.chunkReplace(this, player.location.chunk, Material.valueOf(args[0]), args.drop(1).map { Material.valueOf(it) })
    }

    private fun sectionReplace(player: Player, args: Array<String>) {
        //first of all, is the player in the map?
        val locationPair: Pair<Location?, Location?>? = materialReplaceMap[player]
        if (locationPair == null) {
            player.sendMessage("Sry, you need to select locations with the golden axe first")
            return
        }
        // the player is in the map, are the locations safe?
        if (locationPair.first == null || locationPair.second == null ) {
            // ok then we can replace.
            player.sendMessage("Sry, you need to select locations with the golden axe first")
            return
        }
        // OOOOOOH YES, LETS RUN IT.
        // Chimpanzzze looks kinda confused, i dunno what to say, help me pls.
        replaceWithMaterial(locationPair.first!!, locationPair.second!!, Material.valueOf(args[0]))
    }

    private fun replaceWithMaterial(loc1 : Location, loc2: Location, material: Material) {
        BlockReplacer.replaceArea(this,loc1, loc2, material)
    }

    private fun writeas(args: Array<String>) {
        server.onlinePlayers.forEach { player ->
            player
                .sendMessage("<${args[0]}> ${args.drop(1).joinToString(" ")}")
        }
    }

    private fun sculktrap(player: Player) {
        player.location.clone().add(0.0, -3.0, 0.0).block.type = Material.SCULK_SENSOR
        player.location.clone().add(0.0, -2.0, 0.0).block.type = Material.TNT
        player.sendMessage("sculk trap ready.")
    }

    private fun clearChunk(location: Location, args: Array<String>?) {
        BlockReplacer.chunkReplace(this, location.chunk, Material.AIR, args?.map { s -> Material.valueOf(s) })
    }

    private fun drill(player: Player) {
        myTask = object : BukkitRunnable() {
            override fun run() {
                val location = player.location
                for (z in -DRILL_OFFSET..DRILL_OFFSET) {
                    for (x in -DRILL_OFFSET..DRILL_OFFSET) {
                        for (y in 0..DRILL_OFFSET + 2) {
                            location.clone().add(x.toDouble(), y.toDouble(), z.toDouble()).block.type = Material.AIR
                        }
                    }
                }
                location.block.type = Material.TORCH
            }
        }.runTaskTimer(this, 1, 1)
    }

    private fun draw(player: Player) {
        // in this case we want to activate the command.
        myTask = object : BukkitRunnable() {
            override fun run() {
                val targetBlockExact = player.getTargetBlockExact(20)
                targetBlockExact?.type = player.inventory.itemInMainHand.type
            }
        }.runTaskTimer(this, 1, 1)
    }

    fun replaceBlockOnNextTick(location: Location?, material: Material?) {
        if (location == null || material == null) {
            // cannot, im sry
            return
        }
        object : BukkitRunnable() {
            override fun run() {
                location.block.type = material
            }
        }.runTaskLater(this, 1)
    }

    fun onOPMaterialReplaceTool(player: Player, clickedBlock: Block?, action: Action) {
        // is the player already into the map?
        if (materialReplaceMap[player] == null) {
            // in this case we add the player then
            materialReplaceMap[player] = Pair(null, null)
        }
        val locationPair = materialReplaceMap[player]!!
        // now we have the player for sure
        if (action == Action.LEFT_CLICK_BLOCK) {
            materialReplaceMap[player] = locationPair.copy(clickedBlock?.location, locationPair.second)
            // replace the block pls.
            replaceBlockOnNextTick(clickedBlock?.location, clickedBlock?.type)
            player.sendMessage("First position set")
            return
        }
        if (action == Action.RIGHT_CLICK_BLOCK) {
            // set second position on right click
            materialReplaceMap[player] = locationPair.copy(locationPair.first, clickedBlock?.location)
            player.sendMessage("Second position set")
            return
        }
    }

    fun onOPClearChunkTool(clickedBlock: Block?, action: Action) {
        if (action == Action.LEFT_CLICK_BLOCK) {
            if (clickedBlock != null) {
                clearChunk(clickedBlock.location, null)
            }
        }
    }
}