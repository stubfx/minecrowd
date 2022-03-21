package com.stubfx.plugin.listeners

import com.stubfx.plugin.Main
import com.stubfx.plugin.discord.DiscordModule
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