package net.popzi.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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
}
