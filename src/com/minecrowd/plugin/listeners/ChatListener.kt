package com.minecrowd.plugin.listeners

import com.minecrowd.plugin.Main
import com.minecrowd.plugin.discord.DiscordModule
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent


@Suppress("unused")
class ChatListener(val main: Main) : Listener {

    @EventHandler
    fun onPlayerChatEvent(event: AsyncPlayerChatEvent) {
        DiscordModule.sendToDiscordChat(event.player.name, event.message)
    }

}