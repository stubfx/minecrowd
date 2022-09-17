package com.minecrowd.plugin.listeners

import com.minecrowd.plugin.Main
import com.minecrowd.plugin.listeners.behaviours.PlayerListenerBehaviour
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*


@Suppress("unused")
class PlayerListener(private val main: Main) : Listener {

    private val safeDrop: HashMap<Player, Date> = HashMap()

    // Here im trying to make a workaround for a problem that spigot has.
    // Take a look at this link pls.
    // https://hub.spigotmc.org/jira/browse/SPIGOT-5632
    // because of minecraft and the event Action.LEFT_CLICK_AIR is fired upon player arm movement
    // that is also true while dropping an item, there is no actual way to make distinction between clicking air or dropping item
    // Therefore the actual solution is this:
    // the handler for the drop is going to have a low priority (aka is going to run first), if he was trying to drop a safe object
    // we add the player to the list. On the playerInteractionEvent, if the player is on the list we just remove him and do nothing
    // otherwise we just go on with the normal flow.
    // Oh, another think, the playerInter
    // actEven doesn't get fired while the player is dropping an item but facing a block,
    // so we need to check that, otherwise the player will not be able to throw its next fireball.

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerDropItemEvent(event: PlayerDropItemEvent) {
        if (event.player.inventory.itemInMainHand.type == Material.FIRE_CHARGE) {
            // tried checks like this to avoid the time limit
            // but at this point is actually more safe to just use it.
            //if (event.player.getTargetBlock(null,6).type.isAir) {
            safeDrop[event.player] = Date()
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onOPPlayerEvent(event: PlayerInteractEvent) {
        val player = event.player
        if (player.isOp) {
            PlayerListenerBehaviour.onOPInteractEvent(main, event)
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    fun onPlayerEvent(event: PlayerInteractEvent) {
        // ok, let's check the safe list.
        // if the player is there, that means that he tried to drop the firecharge (atm is just that)
        // check if 2 secs are passed for that player.
        // yes, I know that for tick reason somebody could blow up, but sounds funny, so why not?
        val safetime = (safeDrop[event.player]?.time ?: 0) + 2000
        if (safetime < Date().time) {
            // if we are here, the player was not in the safe drop map.
            PlayerListenerBehaviour.onPlayerInteractEvent(main, event)
        }
    }

}