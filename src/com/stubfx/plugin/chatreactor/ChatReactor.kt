package com.stubfx.plugin.chatreactor

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.impl.*
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.io.IOException
import java.net.InetSocketAddress
import java.util.*

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

    private fun chatCommandResolve(playerName: String, command: String, options: String?) {
        main.runOnBukkit {
            var showTitle = true
            when (command.lowercase()) {
                "spawn" -> Spawn(main, options, playerName).run()
                "dropit" -> DropIt(main, playerName).run()
                "levitate" -> Levitate(main, playerName).run()
                "fire" -> Fire(main, playerName).run()
                "diamonds" -> Diamonds(main, playerName).run()
                "chickens" -> Chickens(main, playerName).run()
                "knock" -> Knock(main, playerName).run()
                "panic" -> PanicSound(main, playerName).run()
                "tree" -> TreeCage(main, playerName).run()
                "speedy" -> Speedy(main, playerName).run()
                "heal" -> Heal(main, playerName).run()
                "hungry" -> Hungry(main, playerName).run()
                "feed" -> Feed(main, playerName).run()
                "wallhack" -> WallHack(main, playerName).run()
                "superman" -> Superman(main, playerName).run()
                "normalman" -> Normalman(main, playerName).run()
                "water" -> Water(main, playerName).run()
                "woollify" -> Woollify(main, playerName).run()
                "randomblock" -> RandomBlock(main, playerName).run()
                "neverfall" -> NeverFall(main, playerName).run()
                "armored" -> Armored(main, playerName).run()
                "tothenether" -> ToTheNether(main, playerName).run()
                "totheoverworld" -> ToTheOverworld(main, playerName).run()
                "bob" -> Bob(main, playerName).run()
                "nukemobs" -> NukeMobs(main, playerName).run()
                "dinnerbone" -> Dinnerbone(main, playerName).run()
                "craftingtable" -> CraftingTable(main, playerName).run()
                "anvil" -> Anvil(main, playerName).run()
                "ihaveit" -> IHaveIt(main, playerName).run()
                "paint" -> Paint(main, playerName).run()
                "goingdown" -> GoingDown(main, playerName).run()
                "nochunknoparty" -> ClearChunk(main, playerName).run()
            }
        }
    }

    fun onDisable() {
        httpserver?.stop(0)
    }

}