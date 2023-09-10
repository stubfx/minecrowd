package com.minecrowd.plugin.chatreactor

import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.minecrowd.plugin.ConfigManager
import com.minecrowd.plugin.PluginUtils
import com.minecrowd.plugin.chatreactor.commands.CommandFactory
import com.minecrowd.plugin.chatreactor.commands.CommandResultWrapper
import com.minecrowd.plugin.chatreactor.commands.StubCommand
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import java.io.IOException
import java.net.InetSocketAddress
import java.util.*


object ChatReactor {

    private var apiKey = ""
    private var serverPort: Int = 8001
    private var httpserver: HttpServer? = null
    private var httpServerSocket: InetSocketAddress? = null
    private const val userCooldown = 1000 * 10
    private val playerCoolDownList: HashMap<String, Long> = hashMapOf()

    private fun checkAndRunChatMessage(userName: String, message: String) {
        if (!message.startsWith("mc")) {
            return
        }
        val regex = """mc\s+(.+?)(\s+(.+))?""".toRegex()
        val match = regex.matchEntire(message)
        val command = match?.groups?.get(1)?.value ?: ""
        val options = match?.groups?.get(2)?.value?.trim() ?: ""
        val chatCommandResolve: CommandResultWrapper = if (PluginUtils.isPlayerAGod(userName)) {
            CommandFactory.forceRun(command, userName, options)
        } else {
            chatCommandResolve(command, userName, options)
        }
        if (chatCommandResolve.showResultMessage) {
            PluginUtils.broadcastMessage(chatCommandResolve.message)
        }
    }


    private fun startServer() {
        // DO NOT REMOVE, THIS SECTION WILL BE USEFUL LATER FOR FUTURE IMPLEMENTATIONS.
        // MAYBE A TWITCH EXTENSION CALLING THE API? DUNNO <3
//        if (!ConfigManager.isChatReactorEnabled()) {
//            PluginUtils.log("chat reactor is currently disabled in the config")
//            return
//        }
//        apiKey = ConfigManager.getApiKey() ?: ""
//        serverPort = ConfigManager.getServerPort()
//        if (apiKey.isEmpty()) {
//            PluginUtils.log("Missing apiKey, chat reactor disabled.")
//            return
//        }
//        PluginUtils.log("Starting chatreactor server at $serverPort")
//        httpServerSocket = InetSocketAddress(serverPort)
//        httpserver = HttpServer.create(httpServerSocket, 0)
//        httpserver?.createContext("/command", MyHandler(this))
//        httpserver?.executor = null // creates a default executor
//        httpserver?.start()
        val twitchClient = TwitchClientBuilder.builder()
            .withEnableChat(true)
            .build()
        twitchClient.chat.joinChannel(ConfigManager.getTwitchChannel()!!)
        twitchClient.eventManager.onEvent(ChannelMessageEvent::class.java) { event ->
            PluginUtils.log("[${event.channel.name}] ${event.user.name}: ${event.message}")
            checkAndRunChatMessage(event.user.name, event.message)
        }
    }

    private fun checkApiKey(apiKey: String?): Boolean {
        return apiKey == this.apiKey
    }

    internal class MyHandler(private val ref: ChatReactor) : HttpHandler {
        @Throws(IOException::class)
        override fun handle(t: HttpExchange) {
            try {

                val query = t.requestURI.query
                val params: HashMap<String, String> = HashMap()
                getParams(query, params)
                if (isKeyValid(params)) {
                    var reply = ""
                    val command = params["command"]!!
                    val playerName = params["name"]!!
                    val options = params["options"]
                    val chatCommandResolve: CommandResultWrapper = if (PluginUtils.isPlayerAGod(playerName)) {
                        CommandFactory.forceRun(command, playerName, options)
                    } else {
                        ref.chatCommandResolve(command, playerName, options)
                    }
                    if (chatCommandResolve.showResultMessage) {
                        reply = chatCommandResolve.message
                    }
                    if (reply.isEmpty()) {
                        // the command has run.
                        t.sendResponseHeaders(204, -1)
                    } else {
                        PluginUtils.broadcastMessage(reply)
                        // there is a message that's needs to be sent back.
                        t.sendResponseHeaders(200, reply.length.toLong())
                        t.responseBody.write(reply.toByteArray())
                    }
                    t.responseBody.close()
                } else {
                    println("[ChatReactor]: wrong apiKey")
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
            StubCommand.commandName(), false,
            "im sorry @$playerName you are still in cooldown.", true
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
        println("resultWrapper.message")
        println(resultWrapper.message)
        println("---------------------")
        return resultWrapper
    }

    private fun isUserInCoolDown(playerName: String): Boolean {
        val time = playerCoolDownList[playerName] ?: return false
        // if the player exists, is the time expired?
        if ((Date().time < time + userCooldown)) {
            // the user is still in coolDown.
            // no update to the list needed.
            return true
        }
        // in this case, the user is in the list but the coolDown is expired.
        // so we remove the user from the list, and we return false.
        playerCoolDownList.remove(playerName)
        return false
    }

    fun onEnable() {
        startServer()
    }

    fun onDisable() {
        httpserver?.stop(0)
    }

}