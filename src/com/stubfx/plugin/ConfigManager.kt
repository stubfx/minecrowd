package com.stubfx.plugin

import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

data class CommandConfig(
    var name: CommandType,
    var title: String,
    var coolDownMillis: Long,
    var isSilent: Boolean,
    val successMessage: String?
)

object ConfigManager {
    private const val config_path = "plugins/stubfx_plugin/stubfx_plugin_config.yml"
    private lateinit var config: YamlConfiguration

    fun getCommand(commandName: CommandType): CommandConfig {
        val command = "commands.$commandName"
        return CommandConfig(
            CommandType.WOOLLIFY,
            config.getString("$command.commanditle") ?: "",
            config.getInt("$command.commandooldown") * 1000L,
            config.get("$command.silent") as Boolean,
            ""
        )
    }

    fun setCommand(commandName: String, commandConfig: CommandConfig) {
        setTitle(commandName, commandConfig.title)
        setCooldown(commandName, commandConfig.coolDownMillis)
        setSilent(commandName, commandConfig.isSilent)
    }

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

    fun isAllowed(command: String): Boolean {
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

    fun getCooldown(command: String): Long {
        return config.getInt("commands.$command.cooldown") * 1000L
    }

    fun setCooldown(command: String, milliseconds: Long) {
        setCooldown(command, (milliseconds / 1000).toInt())
    }

    fun setCooldown(command: String, seconds: Int) {
        config.set("commands.$command.cooldown", seconds)
    }

    fun getTitle(command: String): String {
        return config.getString("commands.$command.title") ?: command
    }

    fun setTitle(command: String, title: String) {
        config.set("commands.$command.title", title)
    }

    fun isSilent(command: String): Boolean {
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

    fun onDisable() {
//        save()
    }
}