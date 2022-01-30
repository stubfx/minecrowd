package com.stubfx.plugin

import com.stubfx.plugin.chatreactor.commands.CommandFactory
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.security.MessageDigest
import java.util.*

data class CommandConfig(
    val type: CommandType,
    var title: String,
    var coolDown: Long,
    var silent: Boolean,
    var enabled: Boolean,
    var showSuccessMessage: Boolean,
    var successMessage: String
)

object ConfigManager {
    private const val config_path = "plugins/stubfx_plugin/stubfx_plugin_config.yml"
    private const val apiKey = "apiKey"
    private lateinit var config: YamlConfiguration

    init {
        try {
            load()
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    private fun load() {
        // loading config file
        val file = File(config_path)
        if (!file.exists()) {
            // if ths file does not exist, then we need to create a new one.
            config = YamlConfiguration()
            PluginUtils.log("No config file found, generating a new one", LogType.WARNING)
            generate()
        } else {
            // in this case the file exists then we need to just patch the commands.
            config = YamlConfiguration.loadConfiguration(file)
            PluginUtils.log("Plugin config loaded")
            // here.
            patchCommands()
            // and then save ffs
            save()
        }
    }

    private fun generate() {
        PluginUtils.log("Generating new plugin config")
        generateApiKey()
        patchCommands()
        PluginUtils.log("Plugin config generated")
        save()
    }

    private fun patchCommands() {
        val commands = CommandFactory.getAvailableCommands()
        PluginUtils.log("Patching commands")

        commands.forEach {
            val command = it.commandType().toString().lowercase()
            val commandTitle = command.replaceFirstChar { c -> c.uppercase() }
            val commandPath = "commands.$command"

            val commandConfig = CommandConfig(
                type = it.commandType(),
                title = config.getString("$commandPath.title", commandTitle)!!,
                coolDown = config.getInt("$commandPath.cooldown", (it.defaultCoolDown() / 1000).toInt()) * 1000L,
                silent = config.getBoolean("$commandPath.silent", false),
                enabled = config.getBoolean("$commandPath.enabled", true),
                showSuccessMessage = config.getBoolean("$commandPath.showSuccessMessage", false),
                successMessage = config.getString("$commandPath.successMessage") ?: "You run the command $commandTitle"
            )

            setCommand(commandConfig)
        }
    }

    private fun generateApiKey() {
        val digest = MessageDigest.getInstance("SHA-256")
        val date = Date().time.toString().reversed()
        val newKey = digest.digest(date.toByteArray()).joinToString("") {
            it.toUByte().toString(16).padStart(2, '0')
        }
        config.set(apiKey, config.getString(apiKey, newKey))
    }

    private fun save() {
        // loading config file
        val file = File(config_path)

        try {
            config.save(file)
        } catch (e: Exception) {
            PluginUtils.log("Error while saving", LogType.ERROR)
            e.printStackTrace()
        }

        PluginUtils.log("Plugin config saved")
    }

    fun getApiKey() : String {
        // must exist.
        return config.getString(apiKey)!!
    }

    fun getCommand(commandType: CommandType): CommandConfig {
        val command: String = commandType.toString().lowercase()
        val commandPath = "commands.$command"
        return CommandConfig(
            commandType,
            config.getString("$commandPath.title") ?: command,
            config.getInt("$commandPath.cooldown") * 1000L,
            config.getBoolean("$commandPath.silent"),
            config.getBoolean("$commandPath.enabled"),
            config.getBoolean("$commandPath.showSuccessMessage"),
            config.getString("$commandPath.successMessage") ?: ""
        )
    }

    fun updateCommand(commandConfig: CommandConfig) {
        setCommand(commandConfig)
        save()
    }

    private fun setCommand(commandConfig: CommandConfig) {
        val commandName: String = commandConfig.type.toString().lowercase()
        val commandPath = "commands.$commandName"

        config.set("$commandPath.title", commandConfig.title)
        config.set("$commandPath.cooldown", (commandConfig.coolDown / 1000).toInt())
        config.set("$commandPath.silent", commandConfig.silent)
        config.set("$commandPath.enabled", commandConfig.enabled)
        config.set("$commandPath.showSuccessMessage", commandConfig.showSuccessMessage)
        config.set("$commandPath.successMessage", commandConfig.successMessage)
    }

    fun onDisable() {

    }
}