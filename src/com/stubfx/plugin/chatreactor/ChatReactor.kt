package com.stubfx.plugin.chatreactor

import com.stubfx.plugin.BlockReplacer
import com.stubfx.plugin.Main
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import java.io.IOException
import java.net.InetSocketAddress
import java.util.*

class ChatReactor(private val main: Main) {

    private val commandCooldown = 0 // to be handled in StreamElements.

    private var lastCommandTime = 0L
    private var apiKey = ""
    private var httpserver: HttpServer? = null
    private var httpServerSocket: InetSocketAddress? = null

    init {
        startServer()
    }

    fun getServer(): Server {
        return main.server
    }

    inline fun forEachPlayer(func: (player: Player) -> Unit) {
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
                ref.chatCommandResolve(params["name"]!!, params["command"]!!)
            } else {
                println("[ChatReactor]: wrong apiKey")
            }
        }
    }

    private fun chatCommandResolve(name: String, command: String) {
        main.runOnBukkit {
            val date = Date().time
            if (date < lastCommandTime + commandCooldown) {
                return@runOnBukkit
            }
            // in this case, COMMAND_COOLDOWN has passed
            // and we can run the command
            var hasCommandRun = true
            when (command.lowercase()) {
                "creeper" -> creeperSpawn()
//                "dropit" -> forceDropItem()
                "levitate" -> levitatePlayer()
                "fire" -> setPlayerOnFire()
                "swap" -> scrambleLocations()
                "diamonds" -> giveDiamonds()
                "fireballs" -> giveFireballs()
                "chickens!" -> chickenInvasion()
                "knock" -> knockbackPlayer()
                "panic" -> playPanicSound()
                "tree" -> generateTreeCage()
                "speedy" -> speedUpPlayer()
                "heal" -> heal()
                "hungry" -> hungry()
                "feed" -> feed()
                "wallhack" -> wallhack()
                "superman" -> giveShitTonOfHearts()
                "normalman" -> revertSuperman()
                "1hp" -> setOneHP()
                "water" -> setWaterBlock()
                "woollify" -> createWoolBubble()
                "randomblock" -> giverandomblock()
                else -> {
                    // in this case the command is not listed above
                    hasCommandRun = false
                }
            }
            if (hasCommandRun) {
                lastCommandTime = date
                forEachPlayer {
                    it.sendTitle(command, name, 10, 70, 20) // ints are def values
                }
            }
        }
    }

    private fun giverandomblock() {
        forEachPlayer {
            forEachPlayer {
                val itemDropped: Item = it.world.dropItemNaturally(it.location, ItemStack(listOf(*Material.values()).random(), 1))
                itemDropped.pickupDelay = 40
            }
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

    private fun createWoolBubble() {
        val wool : List<Material> = listOf(
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
            it.world.generateTree(it.location.add(1.toDouble(),0.toDouble(),0.toDouble()), TreeType.BIG_TREE)
            it.world.generateTree(it.location.add((-1).toDouble(),0.toDouble(),0.toDouble()), TreeType.BIG_TREE)
            it.world.generateTree(it.location.add(0.toDouble(),0.toDouble(),1.toDouble()), TreeType.BIG_TREE)
            it.world.generateTree(it.location.add(0.toDouble(),0.toDouble(), (-1).toDouble()), TreeType.BIG_TREE)
        }
    }

    private fun playPanicSound() {
        val sounds : List<Sound> = listOf(
            Sound.ENTITY_CREEPER_PRIMED,
            Sound.ENTITY_ENDERMAN_SCREAM,
            Sound.ENTITY_SILVERFISH_AMBIENT
        )
        forEachPlayer {
            it.world.spawnParticle(Particle.EXPLOSION_NORMAL, it.location, 3);
            it.world.playSound(it.location, sounds.random(), 3f, 1f);
        }
    }

    private fun knockbackPlayer() {
        forEachPlayer {
            it.velocity = it.location.direction.multiply(-2);
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
        val locations : MutableList<Location> = mutableListOf()
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
            it.fireTicks = 20*20 // secs * avg tics
        }
    }

    private fun forceDropItem() {
        forEachPlayer {
            val item = it.inventory.itemInMainHand
            it.inventory.remove(item)
            val itemDropped: Item = it.world.dropItemNaturally(it.location, item)
            itemDropped.pickupDelay = 100
        }
    }

    private fun creeperSpawn() {
        forEachPlayer {
            it.world.spawnEntity(it.location, EntityType.CREEPER)
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