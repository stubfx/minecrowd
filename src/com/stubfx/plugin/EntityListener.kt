package com.stubfx.plugin

import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType


class EntityListener(val main: Main) : Listener {

//    @EventHandler
//    fun onPlayerJoin(event: PlayerJoinEvent?) {
//
//    }



    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.damager.isOp) {
            // then the attacker is an operator
            val player = event.damager as Player
            if (player.inventory.itemInMainHand.type == Material.STICK) {
                val levitation = PotionEffectType.LEVITATION.createEffect(main.getTicks() * 3, 10)
                (event.entity as LivingEntity).addPotionEffect(levitation)
            }
        }
    }

}