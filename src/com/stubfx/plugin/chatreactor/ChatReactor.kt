package com.stubfx.plugin.chatreactor

import com.stubfx.plugin.ConfigManager
import com.stubfx.plugin.Main
import com.stubfx.plugin.chatreactor.commands.CommandFactory
import com.stubfx.plugin.chatreactor.commands.CommandResultWrapper
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import java.io.IOException
import java.net.InetSocketAddress


class ChatReactor(main: Main) {

    private var apiKey = ""
    private var httpserver: HttpServer? = null
    private var httpServerSocket: InetSocketAddress? = null
    private val commandFactory = CommandFactory(main)

    init {
        startServer()
    }

    private fun startServer() {
        apiKey = ConfigManager.getApiKey()
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
            if (ref.checkApiKey(params["apiKey"])) {
                val chatCommandResolve =
                    ref.chatCommandResolve(params["command"]!!, params["name"]!!, params["options"])
                if (chatCommandResolve.result) {
                    // the command has run.
                    t.sendResponseHeaders(204, -1)
                } else {
                    // the command has not run
                    // may have been in cool down
                    t.sendResponseHeaders(200, chatCommandResolve.message.length.toLong())
                    t.responseBody.write(chatCommandResolve.message.toByteArray())
                }
                t.responseBody.close()
            } else {
                println("[ChatReactor]: wrong apiKey")
            }
        }
    }

    private fun chatCommandResolve(command: String, playerName: String, options: String?): CommandResultWrapper {
        return commandFactory.run(command, playerName, options)
    }

    fun onDisable() {
        httpserver?.stop(0)
    }

}