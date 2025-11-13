package net.popzi.core;

import io.papermc.paper.event.player.PlayerArmSwingEvent;
import net.popzi.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;

/***
 * Our EventDispatcher class has 1 purpose: Listen for events, and dispatch actions within the plugin.
 * No actionable code should *actually* run here. There is probably way better ways of doing this
 * or not doing this at all and registering events all over the place, but this will do for now.
 * Note that we're not extending the Event class as we're not setting up custom events or registering listeners.
 */
public class EventDispatcher implements Listener {
    private final Main main;

    /**
     * Constructor
     * @param main the main plugin instance
     */
    public EventDispatcher(Main main) {
        this.main = main;
    }

    /**
     * Method called to register ourselves to Bukkit once we're ready to do so
     */
    public void Register() {
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
        this.main.MODULE_MANAGER.startEventProcessor();
    }

    /**
     * -------------------------------------------- AVOID USING THIS EVENT --------------------------------------------
     * This has been explicitly put here so that you don't register it.
     * Use EntityDeathEvent instead. Paper had the in-genius idea to have two events with the possibility of
     * returning the same event types, leading to you processing the same event, twice.
     * @param event the player death event
     */
    @EventHandler
    @SuppressWarnings("unused")
    public void OnPlayerDeathEvent(PlayerDeathEvent event) {}

    /**
     * Entity Death Event Dispatching.
     * @implNote the event may also be PlayerDeathEvent.
     * @param event the entity death event
     */
    @EventHandler
    @SuppressWarnings("unused")
    public void onEntityDeathEvent(EntityDeathEvent event) {
        this.main.MODULE_MANAGER.pushEvent(event);
    }

    /**
     * Listens for chunk load events
     * @param event the chunk load event
     */
    @EventHandler
    @SuppressWarnings("unused")
    public void onChunkLoadEvent(ChunkLoadEvent event) {
        this.main.MODULE_MANAGER.pushEvent(event);
    }

    /**
     * Listens for bow shoot events (I.e. skeletons, players)
     * @param event the bow shooting event
     */
    @EventHandler
    @SuppressWarnings("unused")
    public void onEntityShootEvent(EntityShootBowEvent event) {
        this.main.MODULE_MANAGER.pushEvent(event);
    }

    /**
     * Listens for projectiles hitting things (I.e. blocks, or other players or entities)
     * @param event the projectile hitting event
     */
    @EventHandler
    @SuppressWarnings("unused")
    public void onProjectileHitEvent(ProjectileHitEvent event) {
        this.main.MODULE_MANAGER.pushEvent(event);
    }

    /**
     * Listens for entities picking up items
     * @param event the entity picking up the item
     */
    @EventHandler
    @SuppressWarnings("unused")
    public void onItemPickup(EntityPickupItemEvent event) {
        this.main.MODULE_MANAGER.pushEvent(event);
    }

    /**
     * Listens for inventory clicks
     * @param event the inventory click
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    @SuppressWarnings("unused")
    public void onInventoryClickEvent(InventoryClickEvent event) {
        this.main.MODULE_MANAGER.pushEventSingleThreaded(event);
    }

    /**
     * Listens for player join events
     * @param event of the player joining
     */
    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        this.main.MODULE_MANAGER.pushEventSingleThreaded(event);
    }

    /**
     * Listens for player move events
     * @param event of the player moving
     */
    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        this.main.MODULE_MANAGER.pushEvent(event);
    }

    @EventHandler
    public void onPlayerArmSwingEvent(PlayerArmSwingEvent event) {
        this.main.MODULE_MANAGER.pushEvent(event);
    }

    /**
     * Listens for block placements
     * @param event of the player placing a block
     */
    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        this.main.MODULE_MANAGER.pushEventSingleThreaded(event);
    }
}
