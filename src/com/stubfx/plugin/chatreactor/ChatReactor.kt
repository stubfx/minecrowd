package com.stubfx.plugin.chatreactor

import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.CommandResultWrapper
import com.stubfx.plugin.chatreactor.commands.StubCommand
import com.stubfx.plugin.chatreactor.commands.impl.*
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import java.io.IOException
import java.lang.Exception
import java.net.InetSocketAddress


class ChatReactor(private val main: Main) {

    private var apiKey = ""
    private var httpserver: HttpServer? = null
    private var httpServerSocket: InetSocketAddress? = null

    init {
        startServer()
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
            println("test")
            if (ref.checkApiKey(params["apiKey"])) {
                val chatCommandResolve =
                    ref.chatCommandResolve(params["name"]!!, params["command"]!!, params["options"])
                if (chatCommandResolve.result) {
                    // the command has run.
                    t.sendResponseHeaders(204, -1)
                    t.responseBody.close()
                } else {
                    // the command has not run
                    // may have been in cool down
                    t.sendResponseHeaders(200, chatCommandResolve.message.length.toLong())
                    t.responseBody.write(chatCommandResolve.message.toByteArray())
                    t.responseBody.close()
                }
            } else {
                println("[ChatReactor]: wrong apiKey")
            }
        }
    }

    private fun chatCommandResolve(playerName: String, command: String, options: String?): CommandResultWrapper {
        val commandInstance = when (command.lowercase()) {
            "spawn" -> Spawn(main, options, playerName)
            "dropit" -> DropIt(main, playerName)
            "levitate" -> Levitate(main, playerName)
            "fire" -> Fire(main, playerName)
            "diamonds" -> Diamonds(main, playerName)
            "chickens" -> Chickens(main, playerName)
            "knock" -> Knock(main, playerName)
            "panic" -> PanicSound(main, playerName)
            "tree" -> TreeCage(main, playerName)
            "speedy" -> Speedy(main, playerName)
            "heal" -> Heal(main, playerName)
            "hungry" -> Hungry(main, playerName)
            "feed" -> Feed(main, playerName)
            "wallhack" -> WallHack(main, playerName)
            "superman" -> Superman(main, playerName)
            "normalman" -> Normalman(main, playerName)
            "water" -> Water(main, playerName)
            "woollify" -> Woollify(main, playerName)
            "randomblock" -> RandomBlock(main, playerName)
            "neverfall" -> NeverFall(main, playerName)
            "armored" -> Armored(main, playerName)
            "tothenether" -> ToTheNether(main, playerName)
            "totheoverworld" -> ToTheOverworld(main, playerName)
            "bob" -> Bob(main, playerName)
            "nukemobs" -> NukeMobs(main, playerName)
            "dinnerbone" -> Dinnerbone(main, playerName)
            "craftingtable" -> CraftingTable(main, playerName)
            "anvil" -> Anvil(main, playerName)
            "ihaveit" -> IHaveIt(main, playerName)
            "paint" -> Paint(main, playerName)
            "goingdown" -> GoingDown(main, playerName)
            "nochunknoparty" -> ClearChunk(main, playerName)
            else -> StubCommand(main, playerName)
        }
        return try {
            commandInstance.run()
        } catch (e: Exception) {
            println(e)
            CommandResultWrapper("stub", false, "wrong command.")
        }
    }

    fun onDisable() {
        httpserver?.stop(0)
    }

}