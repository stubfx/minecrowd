package com.minecrowd.plugin.chatreactor.commands

import com.minecrowd.plugin.chatreactor.commands.impl.*
import com.minecrowd.plugin.chatreactor.commands.impl.animals.Cat
import com.minecrowd.plugin.chatreactor.commands.impl.animals.Horse
import com.minecrowd.plugin.chatreactor.commands.impl.diamondItems.DiamondSword
import com.minecrowd.plugin.chatreactor.commands.impl.goldenItems.GoldenHoe
import com.minecrowd.plugin.chatreactor.commands.impl.goldenItems.GoldenShovel
import com.minecrowd.plugin.chatreactor.commands.impl.goldenItems.GoldenSword
import com.minecrowd.plugin.chatreactor.commands.impl.teleport.ToTheEnd
import com.minecrowd.plugin.chatreactor.commands.impl.teleport.ToTheNether
import com.minecrowd.plugin.chatreactor.commands.impl.teleport.ToTheOverworld
import com.minecrowd.plugin.chatreactor.commands.impl.weapons.Trident

object CommandFactory {

    private val commandList = listOf(
        Help, Spawn, DropIt, Levitate, Anvil,
        Fire, Diamonds, Chickens, Knock,
        PanicSound, TreeCage, Speedy, Heal,
        Hungry, Feed, WallHack, Superman,
        Normalman, Water, Woollify, RandomBlock,
        NeverFall, Armored, ToTheNether, ToTheOverworld,
        Bob, NukeMobs, Dinnerbone, CraftingTable,
        IHaveIt, Paint, GoingDown, ClearChunk,
        ThatIsTNT, TunnelTime, OpenSpace, UpsideDown, OnTheMoon,
        Cookies, SuperTools, Milk, Potion, Lava, Slowness, Bees, EndFrame, WaterIsLava, LaserView, GoldenSword, GoldenHoe, Clear, CreepyLand, DiamondSword,
        NightVision, Roll, Cat, ToTheEnd, Horse, TimeShift, GoldenShovel, Trident, WhatIsThat, Parkour, BrokenXray, Elytra
    )

    fun getAvailableCommands(): List<Command> {
        return commandList
    }

    private val commandMap: Map<String, Command> = commandList.associateBy { it.commandName().lowercase() }


    fun getCommandOptions(commandName: String): List<String> {
        return getCommand(commandName).tabCompleterOptions()
    }

    fun run(commandName: String, playerName: String, options: String?): CommandResultWrapper {
        return getCommand(commandName).run(playerName, options)
    }

    fun forceRun(commandName: String, playerName: String, options: String?): CommandResultWrapper {
        return getCommand(commandName).forceRun(playerName, options)
    }

    private fun getCommand(commandName: String) = commandMap[commandName.lowercase()] ?: StubCommand

}