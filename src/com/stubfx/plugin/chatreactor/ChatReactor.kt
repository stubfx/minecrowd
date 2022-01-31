package com.stubfx.plugin.chatreactor

import com.stubfx.plugin.ConfigManager
import com.stubfx.plugin.Main
import com.stubfx.plugin.PluginUtils
import com.stubfx.plugin.chatreactor.commands.CommandFactory
import com.stubfx.plugin.chatreactor.commands.CommandResultWrapper
import com.stubfx.plugin.chatreactor.commands.CommandType
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import java.io.IOException
import java.net.InetSocketAddress
import java.util.*
import kotlin.collections.HashMap


class ChatReactor(main: Main) {

    private var apiKey = ""
    private var serverPort : Int = 8001
    private var httpserver: HttpServer? = null
    private var httpServerSocket: InetSocketAddress? = null
    private val defaultCoolDown = 1000*60
    private val playerCoolDownList: HashMap<String, Long> = hashMapOf()

    init {
        startServer()
    }

    private fun startServer() {
        apiKey = ConfigManager.getApiKey()
        serverPort = ConfigManager.getServerPort()
        if (apiKey.isEmpty()) {
            println("Missing apiKey, chat reactor disabled.")
            return
        }
        httpServerSocket = InetSocketAddress(serverPort)
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
            getParams(query, params)
            if (isKeyValid(params)) {
                var reply = ""
                val command = params["command"]!!
                val playerName = params["name"]!!
                val options = params["options"]
                if (command == "help") {
                    // then the user wants the list of commands
                    reply = CommandFactory.getAvailableCommandsNames().joinToString(", ").lowercase()
                } else {
                    val chatCommandResolve = ref.chatCommandResolve(command, playerName, options)
                    if (!chatCommandResolve.result) {
                        reply = chatCommandResolve.message
                    }
                }
                if (reply.isEmpty()) {
                    // the command has run.
                    t.sendResponseHeaders(204, -1)
                } else {
                    // the command has not run
                    // may have been in cool down
                    t.sendResponseHeaders(200, reply.length.toLong())
                    t.responseBody.write(reply.toByteArray())
                }
                t.responseBody.close()
            } else {
                println("[ChatReactor]: wrong apiKey")
            }
        }

        private fun isKeyValid(params: HashMap<String, String>) = ref.checkApiKey(params["apiKey"])

        private fun getParams(query: String, params: HashMap<String, String>) {
            query.split("&").forEach {
                val split = it.split("=")
                params[split[0]] = split[1]
            }
        }
    }

    private fun chatCommandResolve(command: String, playerName: String, options: String?): CommandResultWrapper {
        var resultWrapper = CommandResultWrapper(
            CommandType.STUB, false,
            "im sorry @$playerName, you are still in coolDown."
        )
        // is the user in coolDown
        if (!isUserInCoolDown(playerName)) {
            resultWrapper = CommandFactory.run(command, playerName, options)
        }
        // we don't know if the command has actually run, cause the user may have typed an unknown command
        // therefore we don't want to add the user in the coolDown.
        if (resultWrapper.result) {
            PluginUtils.log("adding $playerName to the cooldown")
            // in this case the command has run, we need to add the user to the coolDown list
            playerCoolDownList[playerName] = Date().time
        }
        return resultWrapper
    }

    private fun isUserInCoolDown(playerName: String): Boolean {
        val time = playerCoolDownList[playerName] ?: return false
        // if the player exists, is the time expired?
        if ((Date().time < time + defaultCoolDown)) {
            // the user is still in coolDown.
            // no update to the list needed.
            return true
        }
        // in this case, the user is in the list but the coolDown is expired.
        // so we remove the user from the list, and we return false.
        playerCoolDownList.remove(playerName)
        return false
    }

    fun onDisable() {
        httpserver?.stop(0)
    }

}