package com.stubfx.plugin

import com.stubfx.plugin.chatreactor.commands.CommandFactory
import com.stubfx.plugin.chatreactor.commands.CommandType
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

data class CommandConfig(
    val name: CommandType,
    val title: String,
    val coolDownMillis: Long,
    val silent: Boolean,
    val enable: Boolean,
    val showSuccessMessage: Boolean,
    val successMessage: String?
)

object ConfigManager {
    private const val config_path = "plugins/stubfx_plugin/stubfx_plugin_config.yml"
    private lateinit var config: YamlConfiguration

    fun getCommand(commandType: CommandType) : CommandConfig {
        val commandName: String = commandType.toString().lowercase()
        val command = "commands.$commandName"
        return CommandConfig(
            CommandType.NOCHUNKNOPARTY,
            config.getString("$command.title") ?: commandName,
            config.getInt("$command.cooldown") * 1000L,
            config.getBoolean("$command.silent"),
            config.getBoolean("$command.enable"),
            config.getBoolean("$command.showSuccessMessage"),
            config.getString("$command.successMessage")
        )
    }

    fun setCommand(commandType: CommandType, commandConfig: CommandConfig) {
        val commandName: String = commandType.toString().lowercase()
        val command = "commands.$commandName"

        config.set("$command.title", commandConfig.title)
        config.set("$command.cooldown", (commandConfig.coolDownMillis/1000).toInt())
        config.set("$command.silent", commandConfig.silent)
        config.set("$command.enable", commandConfig.enable)
        config.set("$command.showSuccessMessage", commandConfig.showSuccessMessage)
        config.set("$command.successMessage", commandConfig.successMessage)

        this.save()
    }

    init {
        load()
        generate()
    }

    fun load() {
        // loading config file
        val file = File(config_path)
        if (!file.exists()) {
            config = YamlConfiguration()
            PluginUtils.log("No config file", "warning")
            generate()
            return
        }

        config = YamlConfiguration.loadConfiguration(file)
        PluginUtils.log("Plugin config loaded","silent")
    }

    private fun generate() {
        val commands = CommandFactory.getAvailableCommands()

        commands.forEach {
            val commandName = it.commandType().toString().lowercase().replaceFirstChar { c -> c.uppercase() }
            val command = it.commandType().toString().lowercase()

            var cooldown = it.defaultCoolDown()
            val commandPath = "commands.$command"
            if (config.get("$commandPath.cooldown") != null) cooldown = config.getInt("$commandPath.cooldown")*1000L

            val commandConfig = CommandConfig(
                name = it.commandType(),
                title = config.getString("$commandPath.title", commandName)!!,
                coolDownMillis = cooldown,
                silent = (config.get("$commandPath.silent") ?: false) as Boolean,
                enable = (config.get("$commandPath.silent") ?: true) as Boolean,
                showSuccessMessage = (config.get("$commandPath.silent") ?: false) as Boolean,
                successMessage = (config.get("$commandPath.title") ?: "You run the command $commandName") as String
            )

            setCommand(it.commandType(), commandConfig)
        }

        PluginUtils.log("Plugin config generated","silent")
    }

    private fun save() {
        // loading config file
        val file = File(config_path)

        try {
            config.save(file)
        } catch (e: Exception) {
            PluginUtils.log("Error while saving", "error")
        }

        PluginUtils.log("Plugin config saved","silent")
    }

    fun onDisable() {
//        save()
    }
}