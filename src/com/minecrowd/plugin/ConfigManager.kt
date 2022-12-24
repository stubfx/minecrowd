package com.minecrowd.plugin

import com.minecrowd.plugin.chatreactor.commands.CommandFactory
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.security.MessageDigest
import java.util.*

data class CommandConfig(
    val name: String,
    var title: String,
    var coolDown: Long,
    var silent: Boolean,
    var enabled: Boolean,
    var cost: Int,
    var showSuccessMessage: Boolean,
    var successMessage: String
)

object ConfigManager {
    private const val reactorPath: String = "chatreactor."
    private const val reactorCommandPath: String = "${reactorPath}.commands."
    private const val discordCommandPath: String = "discord."
    private const val discordCommandEnable: String = "$discordCommandPath.discord-webook-enable"
    private const val discordCommandWebhook: String = "$discordCommandPath.discord-webook"
    private const val config_path = "plugins/minecrowd/minecrowd_config.yml"
    private const val apiKey = "apiKey"
    private const val serverPort = "${reactorPath}.serverPort"
    private const val defaultServerPort = 8001
    private lateinit var config: YamlConfiguration

    init {
        try {
            load()
        } catch (e: Exception) {
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
        } else {
            // in this case the file exists then we need to just patch the commands.
            config = YamlConfiguration.loadConfiguration(file)
            PluginUtils.log("Plugin config loaded")
        }
        // let's make sure the file is not corrupted.
        patchFile()
    }

    private fun patchFile() {
        patchApiKey()
        patchDiscordWebhooks()
        patchServerPort()
        patchChatReactor()
        save()
    }

    private fun patchServerPort() {
        if (config.getString(serverPort) != null) {
            // serverPort already exists, leave it be.
            return
        }
        config.set(serverPort, defaultServerPort)
    }

    private fun patchChatReactor() {
        if (config.getString(apiKey) == null) {
            config.set("$reactorPath.enable", false)
        } else {
            config.set("$reactorPath.enable", true)
        }
        val commands = CommandFactory.getAvailableCommands()
        PluginUtils.log("Patching commands")

        commands.forEach {
            val command = it.commandName()
            val commandTitle = command.replaceFirstChar { c -> c.uppercase() }
            val commandPath = "$reactorCommandPath$command"

            val commandConfig = CommandConfig(
                name = it.commandName(),
                title = config.getString("$commandPath.title", commandTitle)!!,
                coolDown = config.getInt("$commandPath.cooldown", (it.defaultCoolDown / 1000).toInt()) * 1000L,
                silent = config.getBoolean("$commandPath.silent", false),
                enabled = config.getBoolean("$commandPath.enabled", true),
                cost = config.getInt("$commandPath.cost", it.cost),
                showSuccessMessage = config.getBoolean("$commandPath.showSuccessMessage", it.showSuccessMessage ?: false),
                successMessage = config.getString("$commandPath.successMessage", "You run the command $commandTitle")!!
            )

            setCommand(commandConfig)
        }
    }

    private fun patchApiKey() {
        if (config.getString(apiKey) != null) {
            // apikey already exists, leave it be.
            return
        }
        val digest = MessageDigest.getInstance("SHA-256")
        val date = Date().time.toString().reversed()
        val newKey = digest.digest(date.toByteArray()).joinToString("") {
            it.toUByte().toString(16).padStart(2, '0')
        }
        config.set(apiKey, newKey)
    }

    private fun patchDiscordWebhooks() {
        if (config.getString(discordCommandEnable) == null) {
            config.set(discordCommandEnable, false)
            config.set(
                discordCommandWebhook,
                "https://discord.com/api/webhooks/234234234234234234/fasfsdafasdfdsafsdafadsfasd"
            )
        }
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

    fun getApiKey(): String {
        // must exist.
        return config.getString(apiKey)!!
    }

    fun getServerPort(): Int {
        // must exist.
        return config.getInt(serverPort, 8001)
    }

    fun getCommand(commandType: String): CommandConfig {
        val command: String = commandType.lowercase()
        val commandPath = "$reactorCommandPath$command"
        return CommandConfig(
            commandType,
            config.getString("$commandPath.title") ?: command,
            config.getInt("$commandPath.cooldown") * 1000L,
            config.getBoolean("$commandPath.silent"),
            config.getBoolean("$commandPath.enabled"),
            config.getInt("$commandPath.cost"),
            config.getBoolean("$commandPath.showSuccessMessage"),
            config.getString("$commandPath.successMessage") ?: ""
        )
    }

    fun updateCommand(commandConfig: CommandConfig) {
        setCommand(commandConfig)
        save()
    }

    private fun setCommand(commandConfig: CommandConfig) {
        val commandName: String = commandConfig.name.lowercase()
        val commandPath = "$reactorCommandPath$commandName"

        config.set("$commandPath.title", commandConfig.title)
        config.set("$commandPath.cooldown", (commandConfig.coolDown / 1000).toInt())
        config.set("$commandPath.silent", commandConfig.silent)
        config.set("$commandPath.enabled", commandConfig.enabled)
        config.set("$commandPath.cost", commandConfig.cost)
        config.set("$commandPath.showSuccessMessage", commandConfig.showSuccessMessage)
        config.set("$commandPath.successMessage", commandConfig.successMessage)
    }

    fun onDisable() {

    }

    fun isChatReactorEnabled(): Boolean {
        return config.getBoolean("$reactorPath.enable", false)
    }

    fun isDiscordWebhookEnabled(): Boolean {
        return config.getBoolean(discordCommandEnable, false)
    }

    fun getDiscordWebhook(): String {
        return config.getString(discordCommandWebhook, "")!!
    }

}