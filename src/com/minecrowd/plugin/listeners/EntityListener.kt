package com.minecrowd.plugin.listeners

import com.minecrowd.plugin.BlockReplacer
import com.minecrowd.plugin.Main
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffectType


@Suppress("unused")
class EntityListener(val main: Main) : Listener {

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.damager.type === EntityType.PLAYER) {
            val player = event.damager as Player
            val itemInMainHand = player.inventory.itemInMainHand
            if (event.entity !is LivingEntity) return
            val livingEntity = event.entity as LivingEntity
            if (itemInMainHand.type == Material.GOLDEN_SWORD) {
                println(itemInMainHand.itemMeta)
                val levitation = PotionEffectType.LEVITATION.createEffect(main.getTicks() * 3, 50)
                livingEntity.addPotionEffect(levitation)
            } else if (itemInMainHand.type == Material.GOLDEN_SHOVEL) {
                livingEntity.world.spawnEntity(livingEntity.location, EntityType.PRIMED_TNT)
            } else if  (itemInMainHand.type == Material.DIAMOND_SWORD) {
                val dim = 2.0
                val loc1 = livingEntity.location.subtract(dim, dim, dim)
                val loc2 = livingEntity.location.add(dim, dim, dim)
                BlockReplacer.replaceAreaAsync(loc1, loc2, Material.GLASS)
            }
        }
    }
}