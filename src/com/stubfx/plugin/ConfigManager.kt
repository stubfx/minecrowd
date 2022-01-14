package com.stubfx.plugin

import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import kotlin.math.round

object ConfigManager {
    private const val config_path = "plugins/stubfx_plugin_config.yml"
    private lateinit var config: YamlConfiguration

    init {
        load()
    }

    fun load() {
        // loading config file
        val file = File(config_path)
        if (!file.exists()) {
            config = YamlConfiguration()
            Utils.log("No config file", "warning")
            return
        }

        config = YamlConfiguration.loadConfiguration(file)
        Utils.log("Plugin config loaded")
    }

    fun isAllowed(command: String) : Boolean {
        val list = config["commands"] as MemorySection
        val commandOptions = list.get(command) as MemorySection
        val allowed = commandOptions.get("enable")

        return allowed == true
    }

    fun enableCommand(command: String) {
        config.set("commands.$command.enable", true)
    }

    fun disableCommand(command: String) {
        config.set("commands.$command.enable", false)
    }

    fun getCooldown(command: String) : Long {
        return config.getInt("commands.$command.cooldown") * 1000L
    }

    fun setCooldown(command: String, milliseconds: Long) {
        setCooldown(command, (milliseconds/1000).toInt())
    }

    fun setCooldown(command: String, seconds: Int) {
        config.set("commands.$command.cooldown", seconds)
    }

    fun getTitle(command: String) : String {
        return config.getString("commands.$command.title") ?: command
    }

    fun setTitle(command: String, title: String) {
        config.set("commands.$command.title", title)
    }

    fun isSilent(command: String) : Boolean {
        return config.get("commands.$command.silent") as Boolean
    }

    fun setSilent(command: String, isSilent: Boolean) {
        config.set("commands.$command.silent", isSilent)
    }

    fun save() {
        // loading config file
        val file = File(config_path)

        try {
            config.save(file)
        } catch (e: Exception) {
            Utils.log("Error while saving", "error")
        }

        Utils.log("Plugin config saved")
    }
}