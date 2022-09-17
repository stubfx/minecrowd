package com.minecrowd.plugin.discord

import com.google.gson.JsonObject
import com.minecrowd.plugin.ConfigManager
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse
import java.time.Duration


object DiscordModule {

    private val enabled = ConfigManager.isDiscordWebhookEnabled()
    private val webhook = ConfigManager.getDiscordWebhook()
    var client: HttpClient? = null

    fun onEnable() {
        if (!enabled) return
        client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build()
    }

    fun onDisable() {
        client = null
    }

    fun sendToDiscordChat(sender: String, msg: String) {
        if (!enabled) return
        val body = JsonObject()
        // https://discord.com/developers/docs/resources/webhook#execute-webhook
        body.addProperty("username", sender)
        body.addProperty("content", msg)
        println(body.toString())
        val request = HttpRequest.newBuilder()
            .uri(URI.create(webhook))
            .timeout(Duration.ofMinutes(2))
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(body.toString()))
            .build()
        client?.sendAsync(request, HttpResponse.BodyHandlers.ofString())//?.thenAccept(System.out::println)
    }

}