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
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.io.IOException
import java.net.InetSocketAddress
import java.util.*


class Main : JavaPlugin() {

    private val commandCooldown = 2*1000*60 // 2 mins cooldown in millis
    private var lastCommandTime = 0L
    private var apyKey = "ERROR"
    private var httpserver : HttpServer? = null

    private fun startServer() {
        httpserver = HttpServer.create(InetSocketAddress(8001), 0)
        httpserver?.createContext("/command", MyHandler(this))
        httpserver?.executor = null // creates a default executor
        httpserver?.start()
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
                if (date < lastCommandTime + commandCooldown){
                    return
                }
                // in this case, COMMAND_COOLDOWN has passed
                // and we can run the command
                lastCommandTime = date
                when (command.lowercase()) {
                    "creeper" -> creeperSpawn()
                }
            }
        }.runTask(this)
    }

    override fun onEnable() {
        apyKey = System.getenv("apiKey")
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
        httpserver?.stop(0)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        when (command.name.lowercase()) {
            "creep" -> creeperSpawn()
        }
        return false
    }

    private fun creeperSpawn() {
        server.onlinePlayers.forEach { player ->
            player.world.spawnEntity(player.location, EntityType.CREEPER)
        }
    }
}