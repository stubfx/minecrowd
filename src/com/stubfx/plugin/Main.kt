package com.stubfx.plugin

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class Main : JavaPlugin() {

    private val DRILL_OFFSET = 2
    private var myTask: BukkitTask? = null

    private var materialReplaceMap: HashMap<Player, Pair<Location?, Location?>> = HashMap()

    override fun onEnable() {
        getCommand("clearchunk")?.tabCompleter = MyMaterialTabCompleter()
        getCommand("chunkreplace")?.tabCompleter = MyMaterialTabCompleter()
        server.pluginManager.registerEvents(PlayerListener(this), this)
        server.pluginManager.registerEvents(EntityListener(this), this)
    }

    fun checkLocationsWorld(l1: Location, l2: Location) {
        if (l1.world != l2.world) {
            throw IllegalArgumentException("both locations must be from the same world")
        }
    }

    fun getMinLocation(l1: Location, l2: Location): Location{
        checkLocationsWorld(l1, l2)
        return Location(l1.world, minOf(l1.x, l2.x), minOf(l1.y, l2.y), minOf(l1.z, l2.z))
    }

    fun getMaxLocation(l1: Location, l2: Location): Location{
        checkLocationsWorld(l1, l2)
        return Location(l1.world, maxOf(l1.x, l2.x), maxOf(l1.y, l2.y), maxOf(l1.z, l2.z))
    }

    fun consoleLog(msg: String) {
        server.consoleSender.sendMessage("LOG: $msg")
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
            "jesus" -> jesus(player)
            "drill" -> drill(player)
            "clearchunk" -> clearChunkBelow(player.location, args)
            "sculktrap" -> sculktrap(player)
            "writeas" -> writeas(command, args)
            "woolreplace" -> sectionReplace(player, args)
            "chunkreplace" -> chunkreplace(player, args)
            "stubstop" -> clearTask()
        }
    }

    private fun chunkreplace(player: Player, args: Array<String>) {
        var tmp = object : BukkitRunnable(){
            override fun run() {
                // get the chunk of the player
                val chunk = player.location.chunk
                for (x in 0..15) {
                    for (z in 0..15) {
                        for (y in 255 downTo 1) {
                            // this runs for any selected block of the chunk
                            // we need to convert to air, only if they are not ores
                            val block = chunk.getBlock(x, y, z)
                            // check if block type is not contained in the 'exclude list'
                            if (block.type != Material.AIR &&
                                block.type != Material.CAVE_AIR &&
                                block.type != Material.VOID_AIR ) {
                                // if is not air, just replace it.
                                block.type = Material.valueOf(args[0])
                            }
                        }
                    }
                }
            }
        }.runTask(this)
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

    fun replaceWithMaterial(loc1 : Location, loc2: Location, material: Material) {
        myTask = object : BukkitRunnable() {
            override fun run() {
                checkLocationsWorld(loc1, loc2)
                val minL = getMinLocation(loc1, loc2)
                val maxL = getMaxLocation(loc1, loc2)
                for (z in minL.z.toInt()..maxL.z.toInt()) {
                    for (x in minL.x.toInt()..maxL.x.toInt()) {
                        for (y in minL.y.toInt()..maxL.y.toInt()) {
                            val block = Location(loc1.world, x.toDouble(), y.toDouble(), z.toDouble()).block
                            if (block.type != Material.AIR) {
                                // if is not air, just replace it.
                                Location(loc2.world, x.toDouble(), y.toDouble(), z.toDouble()).block.type = material
                            }
                        }
                    }
                }
            }
        }.runTask(this)
    }

    private fun writeas(command: Command, args: Array<String>) {
        var isFirst = true
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

    private fun clearChunkBelow(location: Location, args: Array<String>?, clearFullChunk: Boolean = false) {
        val playerHeight = if (!clearFullChunk) location.y else 255
        val chunk = location.chunk
        myTask = object : BukkitRunnable() {
            override fun run() {
                val materialsToExclude = mutableListOf<Material>()
                if (args != null) {
                    for (arg in args) {
                        materialsToExclude.add(Material.valueOf(arg))
                    }
                }
                for (x in 0..15) {
                    for (z in 0..15) {
                        for (y in playerHeight.toInt() downTo 1) {
                            // this runs for any selected block of the chunk
                            // we need to convert to air, only if they are not ores
                            val block = chunk.getBlock(x, y, z)
                            // check if block type is not contained in the 'exclude list'
                            if (!materialsToExclude.contains(block.type)) {
                                // here if the block is not what we are looking for
                                // so we convert that into air
                                block.type = Material.AIR
                            }
                        }
                    }
                }
            }
        }.runTask(this)
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

    private fun jesus(player: Player) {
        myTask = object : BukkitRunnable() {
            override fun run() {
                player.location.add(0.0, -1.0, 0.0).block.type = Material.STONE
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

    private fun replaceBlockOnNextTick(location: Location?, material: Material?) {
        if (location == null || material == null) {
            // cannot, im sry
            return
        }
        val tmp = object : BukkitRunnable() {
            override fun run() {
                location.block.type = material
            }
        }.runTaskLater(this, 1)
    }

    fun onPlayerInteractEvent(event: PlayerInteractEvent) {
        // if player is not op, just quit.
        // or if action is not Action.LEFT_CLICK_BLOCK a block, just quit.
        if (!event.player.isOp) return
        val player = event.player
        val clickedBlock = event.clickedBlock
        when (player.inventory.itemInMainHand.type) {
            Material.GOLDEN_SHOVEL -> {
                if (event.action == Action.LEFT_CLICK_BLOCK) {
                    if (clickedBlock != null) {
                        clearChunkBelow(clickedBlock.location, null, true)
                    }
                }
            }
            Material.GOLDEN_AXE -> {
                // is the player already into the map?
                if (materialReplaceMap[player] == null) {
                    // in this case we add the player then
                    materialReplaceMap[player] = Pair(null, null)
                }
                val locationPair = materialReplaceMap[player]!!
                // now we have the player for sure
                if (event.action == Action.LEFT_CLICK_BLOCK) {
                    materialReplaceMap[player] = locationPair.copy(clickedBlock?.location, locationPair.second)
                    // replace the block pls.
                    replaceBlockOnNextTick(clickedBlock?.location, clickedBlock?.type)
                    player.sendMessage("First position set")
                    return
                }
                if (event.action == Action.RIGHT_CLICK_BLOCK) {
                    // set second position on right click
                    materialReplaceMap[player] = locationPair.copy(locationPair.first, clickedBlock?.location)
                    player.sendMessage("Second position set")
                    return
                }
            }
            else -> {
                // just do nothing for the moment.
            }
        }
    }
}