package com.minecrowd.plugin.listeners

import com.minecrowd.plugin.Main
import org.bukkit.event.Listener


@Suppress("unused")
class EntityListener(val main: Main) : Listener {

//    @EventHandler
//    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
//        if (event.damager.isOp) {
//            // then the attacker is an operator
//            val player = event.damager as Player
//            if (player.inventory.itemInMainHand.type == Material.STICK) {
//                val levitation = PotionEffectType.LEVITATION.createEffect(main.getTicks() * 3, 50)
//                (event.entity as LivingEntity).addPotionEffect(levitation)
//            }
//        }
//    }

}