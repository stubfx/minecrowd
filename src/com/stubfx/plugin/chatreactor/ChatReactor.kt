package com.stubfx.plugin.chatreactor

import com.stubfx.plugin.BlockReplacer
import com.stubfx.plugin.Main
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.entity.*
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.io.IOException
import java.net.InetSocketAddress
import java.util.*
import kotlin.random.Random

class ChatReactor(private val main: Main) {

    private var apiKey = ""
    private var httpserver: HttpServer? = null
    private var httpServerSocket: InetSocketAddress? = null

    companion object {

        private var currentTask: BukkitTask? = null
        private var main: Main? = null
        private var currentTaskTickCount = 0

        fun runOnBukkitEveryTick(func: () -> Unit, duration: Int): BukkitTask {
            val taskDuration = main!!.getTicks() * duration
            return object : BukkitRunnable() {
                override fun run() {
                    currentTaskTickCount++
                    func()
                    if (currentTaskTickCount > taskDuration) {
                        this.cancel()
                    }
                }
            }.runTaskTimer(main!!, 1, 1)
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

        fun setMainRef(main: Main) {
            this.main = main
        }

    }


    init {
        startServer()
        setMainRef(main)
    }

    fun getCloseLocationFromPlayer(location: Location): Location {
        val x = Random.nextDouble(-10.0, 10.0)
        val y = Random.nextDouble(1.0, 10.0)
        val z = Random.nextDouble(-10.0, 10.0)
        return location.add(x, y, z)
    }

    fun getServer(): Server {
        return main.server
    }

    private inline fun forEachPlayer(func: (player: Player) -> Unit) {
        getServer().onlinePlayers.forEach { func(it) }
    }

    private fun startServer() {
        apiKey = System.getenv("mc_apiKey") ?: ""
        if (apiKey.isEmpty()) {
            println("Missing apiKey, chat reactor disabled.")
            return
        }
        httpServerSocket = InetSocketAddress(8001)
        httpserver = HttpServer.create(httpServerSocket, 0)
        httpserver?.createContext("/command", MyHandler(this))
        httpserver?.executor = null // creates a default executor
        httpserver?.start()
    }

    private fun checkApiKey(apiKey: String?): Boolean {
        return apiKey == this.apiKey
    }

    internal class MyHandler(private val ref: ChatReactor) : HttpHandler {
        @Throws(IOException::class)
        override fun handle(t: HttpExchange) {
            val query = t.requestURI.query
            val params: HashMap<String, String> = HashMap()
            query.split("&").forEach {
                val split = it.split("=")
                params[split[0]] = split[1]
            }
            t.sendResponseHeaders(204, -1)
            t.responseBody.close()
            if (ref.checkApiKey(params["apiKey"])) {
                ref.chatCommandResolve(params["name"]!!, params["command"]!!, params["options"])
            } else {
                println("[ChatReactor]: wrong apiKey")
            }
        }
    }

    private fun chatCommandResolve(name: String, command: String, options: String?) {
        main.runOnBukkit {
            var showTitle = true
            when (command.lowercase()) {
                "spawn" -> mobspawn(options)
                "dropit" -> forceDropItem()
                "levitate" -> levitatePlayer()
                "fire" -> setPlayerOnFire()
                "diamonds" -> giveDiamonds()
                "chickens" -> chickenInvasion()
                "knock" -> knockbackPlayer()
                "panic" -> {
                    showTitle = false
                    playPanicSound()
                }
                "tree" -> generateTreeCage()
                "speedy" -> speedUpPlayer()
                "heal" -> heal()
                "hungry" -> hungry()
                "feed" -> feed()
                "wallhack" -> wallhack()
                "superman" -> giveShitTonOfHearts()
                "normalman" -> revertSuperman()
                "water" -> setWaterBlock()
                "woollify" -> woollify()
                "randomblock" -> giverandomblock()
                "neverfall" -> neverFall()
                "armored" -> armored()
                "tothenether" -> toTheNether()
                "totheoverworld" -> toTheOverworld()
                "bob" -> spawnBob()
                "nukemobs" -> nukeMobs()
                "dinnerbone" -> applyDinnerbone()
                "craftingtable" -> craftingLand()
                "anvil" -> spawnAnvilOnTop()
                "ihaveit" -> iHaveIt()
                "paint" -> paint()
                "goingdown" -> goingDown()
//                "swap" -> scrambleLocations()
//                "fireballs" -> giveFireballs()
//                "1hp" -> setOneHP()
//                "nochunknoparty" -> clearChunk()
                else -> {
                    // in this case the command is not listed above
                    showTitle = false
                }
            }
            if (showTitle) {
                forEachPlayer {
                    it.sendTitle(command.uppercase(), name, 10, 70, 20) // ints are def values
                }
            }
        }
    }

    private fun paint() {
        val wool: Material = listOf(
            Material.WHITE_WOOL,
            Material.BLUE_WOOL,
            Material.RED_WOOL,
            Material.CYAN_WOOL,
            Material.GRAY_WOOL,
        ).random()
        startRecurrentTask {
            forEachPlayer {
                it.getTargetBlockExact(100)?.type = wool
            }
        }
    }

    private fun goingDown() {
        startShortRecurrentTask {
            forEachPlayer {
                it.location.subtract(0.0, 1.0, 0.0).block.type = Material.AIR
            }
        }
    }

    private fun iHaveIt() {
        startRecurrentTask {
            forEachPlayer {
                val type = it.getTargetBlockExact(100)?.type ?: return@forEachPlayer
                it.inventory.addItem(ItemStack(type))
            }
        }
    }

    private fun spawnAnvilOnTop() {
        forEachPlayer {
            it.location.add(0.0, 5.0, 0.0).block.type = Material.ANVIL
        }
    }

    private fun clearChunk() {
        forEachPlayer {
            val chunk = it.getTargetBlockExact(100)?.chunk ?: return
            BlockReplacer.chunkReplace(main, chunk, Material.AIR)
        }
    }

    private fun craftingLand() {
        forEachPlayer {
            val loc1 = it.location.subtract(20.0, 20.0, 20.0)
            val loc2 = it.location.add(20.0, 20.0, 20.0)
            BlockReplacer.replaceAreaExAir(main, loc1, loc2, Material.CRAFTING_TABLE)
        }
    }

    private fun applyDinnerbone() {
        forEachPlayer { player ->
            player.getNearbyEntities(100.0, 100.0, 100.0).forEach {
                if (it is LivingEntity) {
                    it.customName = "Dinnerbone"
                    it.isCustomNameVisible = false
                }
            }
        }
    }

    private fun spawnBob() {
        forEachPlayer {
            val zombie = it.world.spawnEntity(it.location, EntityType.ZOMBIE) as Zombie
            val chicken = it.world.spawnEntity(it.location, EntityType.CHICKEN) as Chicken
            zombie.setBaby()
            zombie.customName = "bob"
            zombie.isCustomNameVisible = true
            zombie.equipment?.helmet = ItemStack(Material.DIAMOND_HELMET)
            zombie.equipment?.chestplate = ItemStack(Material.DIAMOND_CHESTPLATE)
            zombie.equipment?.leggings = ItemStack(Material.DIAMOND_LEGGINGS)
            zombie.equipment?.boots = ItemStack(Material.DIAMOND_BOOTS)
            zombie.equipment?.setItemInMainHand(ItemStack(Material.NETHERITE_SWORD))
            chicken.addPassenger(zombie)
        }
    }

    private fun nukeMobs() {
        forEachPlayer { player ->
            player.getNearbyEntities(100.0, 100.0, 100.0).forEach {
                if (it is LivingEntity) {
                    it.health = 0.0
                }
            }
        }
    }

    private fun toTheNether() {
        forEachPlayer {
            it.teleport(main.server.getWorld("world_nether")?.spawnLocation ?: it.location)
        }
    }

    private fun toTheOverworld() {
        forEachPlayer {
            it.teleport(main.server.getWorld("world")?.spawnLocation ?: it.location)
        }
    }

    private fun armored() {
        forEachPlayer {
            it.inventory.helmet = ItemStack(Material.NETHERITE_HELMET)
            it.inventory.chestplate = ItemStack(Material.NETHERITE_CHESTPLATE)
            it.inventory.leggings = ItemStack(Material.NETHERITE_LEGGINGS)
            it.inventory.boots = ItemStack(Material.NETHERITE_BOOTS)
            it.inventory.setItemInMainHand(ItemStack(Material.NETHERITE_SWORD))
        }
    }

    private fun neverFall() {
        startRecurrentTask {
            forEachPlayer {
                it.location.subtract(0.0, 1.0, 0.0).block.type = Material.ORANGE_WOOL
            }
        }
    }

    private fun giverandomblock() {
        forEachPlayer {
            val itemDropped: Item =
                it.world.dropItemNaturally(it.location, ItemStack(listOf(*Material.values()).random(), 1))
            itemDropped.pickupDelay = 40
        }
    }

    private fun wallhack() {
        forEachPlayer { player ->
            player.getNearbyEntities(200.0, 200.0, 200.0).forEach {
                if (it is LivingEntity) {
                    it.addPotionEffect(PotionEffectType.GLOWING.createEffect(main.getTicks() * 20, 1))
                }
            }
        }
    }

    private fun giveFireballs() {
        forEachPlayer {
            val itemDropped: Item = it.world.dropItemNaturally(it.location, ItemStack(Material.FIRE_CHARGE, 30))
            itemDropped.pickupDelay = 40
        }
    }

    private fun woollify() {
        val wool: List<Material> = listOf(
            Material.WHITE_WOOL,
            Material.BLUE_WOOL,
            Material.RED_WOOL,
            Material.CYAN_WOOL,
            Material.GRAY_WOOL,
        )
        forEachPlayer {
            val loc1 = it.location.subtract(20.0, 20.0, 20.0)
            val loc2 = it.location.add(20.0, 20.0, 20.0)
            BlockReplacer.replaceAreaExAir(main, loc1, loc2, wool.random())
        }
    }

    private fun setWaterBlock() {
        forEachPlayer {
            it.location.block.type = Material.WATER
        }
    }

    private fun setOneHP() {
        forEachPlayer {
            it.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 1.0
        }
    }

    private fun revertSuperman() {
        forEachPlayer {
            it.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 20.0
        }
    }

    private fun feed() {
        forEachPlayer {
            it.foodLevel = 100
        }
    }

    private fun giveShitTonOfHearts() {
        forEachPlayer {
            it.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 200.0
        }
        // let's fix it.
        heal()
    }

    private fun hungry() {
        forEachPlayer {
            it.addPotionEffect(PotionEffectType.HUNGER.createEffect(main.getTicks() * 10, 50))
        }
    }

    private fun heal() {
        forEachPlayer {
            val heal = PotionEffectType.HEAL.createEffect(main.getTicks() * 20, 50)
            it.addPotionEffect(heal)
        }
    }

    private fun speedUpPlayer() {
        forEachPlayer {
            val speed = PotionEffectType.SPEED.createEffect(main.getTicks() * 20, 50)
            it.addPotionEffect(speed)
        }
    }

    private fun generateTreeCage() {
        forEachPlayer {
            it.world.generateTree(it.location.add(1.toDouble(), 0.toDouble(), 0.toDouble()), TreeType.BIG_TREE)
            it.world.generateTree(it.location.add((-1).toDouble(), 0.toDouble(), 0.toDouble()), TreeType.BIG_TREE)
            it.world.generateTree(it.location.add(0.toDouble(), 0.toDouble(), 1.toDouble()), TreeType.BIG_TREE)
            it.world.generateTree(it.location.add(0.toDouble(), 0.toDouble(), (-1).toDouble()), TreeType.BIG_TREE)
        }
    }

    private fun playPanicSound() {
        val sounds: List<Sound> = listOf(
            Sound.ENTITY_CREEPER_PRIMED,
            Sound.ENTITY_ENDERMAN_SCREAM,
            Sound.ENTITY_SILVERFISH_AMBIENT,
            Sound.BLOCK_END_PORTAL_SPAWN,
            Sound.ENTITY_WITHER_SHOOT,
            Sound.ENTITY_GHAST_WARN
        )
        forEachPlayer {
            it.world.spawnParticle(Particle.EXPLOSION_NORMAL, it.location, 3)
            it.world.playSound(it.location, sounds.random(), 3f, 1f)
        }
    }

    private fun knockbackPlayer() {
        forEachPlayer {
            it.velocity = it.location.direction.multiply(-2)
        }
    }

    private fun chickenInvasion() {
        forEachPlayer {
            for (i in 0..20) {
                it.world.spawnEntity(it.location, EntityType.CHICKEN)
            }
        }
    }

    private fun scrambleLocations() {
        val locations: MutableList<Location> = mutableListOf()
        forEachPlayer {
            locations.add(it.location)
        }
        // then we need to scramble the locations.
        forEachPlayer {
            it.teleport(locations.random())
        }
    }

    private fun giveDiamonds() {
        forEachPlayer {
            val itemDropped: Item = it.world.dropItemNaturally(it.location, ItemStack(Material.DIAMOND, 2))
            itemDropped.pickupDelay = 40
        }
    }

    private fun setPlayerOnFire() {
        forEachPlayer {
            it.fireTicks = 20 * 20 // secs * avg tics
        }
    }

    private fun forceDropItem() {
        forEachPlayer {
            val item = it.inventory.itemInMainHand
            if (!item.type.isAir) {
                it.inventory.remove(item)
                val itemDropped: Item = it.world.dropItemNaturally(it.location, item)
                itemDropped.pickupDelay = 100
            }
        }
    }

    private fun mobspawn(options: String?) {
        val blacklist = listOf(EntityType.WITHER, EntityType.ENDER_DRAGON)
        EntityType.GLOW_SQUID
        var mobToSpawn = EntityType.CREEPER
        try {
            mobToSpawn = EntityType.valueOf(options?.uppercase() ?: "CREEPER")
        } catch (_: Exception) {
        }
        if (blacklist.contains(mobToSpawn)) {
            return
        }
        forEachPlayer {
            it.world.spawnEntity(it.location, mobToSpawn)
        }
    }

    private fun levitatePlayer() {
        forEachPlayer {
            val levitation = PotionEffectType.LEVITATION.createEffect(main.getTicks() * 5, 3)
            it.addPotionEffect(levitation)
        }

    }

    fun onDisable() {
        httpserver?.stop(0)
    }

}