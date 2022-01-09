package com.stubfx.plugin

import com.stubfx.plugin.listeners.EntityListener
import com.stubfx.plugin.listeners.PlayerListener
import com.stubfx.plugin.listeners.ProjectileListener
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.io.IOException
import java.net.InetSocketAddress
import java.util.*


class Main : JavaPlugin() {

    private val COMMAND_COOLDOWN = 2*1000*60;// 2 mins cooldown in millis
    private var LAST_COMMAND_TIME = 0L
    private var API_KEY = "ERROR"
    private var HTTP_SERVER : HttpServer? = null

    private fun startServer() {
        HTTP_SERVER = HttpServer.create(InetSocketAddress(8001), 0)
        HTTP_SERVER?.createContext("/command", MyHandler(this))
        HTTP_SERVER?.executor = null // creates a default executor
        HTTP_SERVER?.start()
    }

    internal class MyHandler(private val main: Main) : HttpHandler {
        @Throws(IOException::class)
        override fun handle(t: HttpExchange) {
            val command = t.requestURI.query
            t.sendResponseHeaders(204, -1)
            t.responseBody.close()
            main.chatCommandResolve(command)
        }
    }

    private fun chatCommandResolve(command: String) {
        object : BukkitRunnable() {
            override fun run() {
                val date = Date().time
                if (date < LAST_COMMAND_TIME + COMMAND_COOLDOWN){
                    return
                }
                // in this case, COMMAND_COOLDOWN has passed
                // and we can run the command
                LAST_COMMAND_TIME = date
                when (command.lowercase()) {
                    "creeper" -> creeperSpawn()
                }
            }
        }.runTask(this)
    }

    override fun onEnable() {
        API_KEY = System.getenv("apiKey")
        startServer()
        getCommand("clearchunk")?.tabCompleter = MaterialTabCompleter()
        getCommand("sectionreplace")?.tabCompleter = MaterialTabCompleter()
        getCommand("chunkreplace")?.tabCompleter = MaterialTabCompleter()
        server.pluginManager.registerEvents(PlayerListener(this), this)
        server.pluginManager.registerEvents(EntityListener(this), this)
        server.pluginManager.registerEvents(ProjectileListener(this), this)
    }

    fun getTicks(): Int {
        return 20
    }

    override fun onDisable() {
        HTTP_SERVER?.stop(0)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        // in this case we found me.
        checkCommand(sender as Player, command, args)
        return false
    }

    private fun checkCommand(player: Player, command: Command, args: Array<String>) {
        when (command.name.lowercase()) {
            "creep" -> creeperSpawn()
        }
    }

    private fun creeperSpawn() {
        server.onlinePlayers.forEach { player ->
            player.world.spawnEntity(player.location, EntityType.CREEPER)
        }
    }
}