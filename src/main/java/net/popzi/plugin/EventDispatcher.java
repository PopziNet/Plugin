package net.popzi.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

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
    }

    /**
     * Player Death Event Dispatching
     * @param event the player death event
     */
    @EventHandler
    public void OnPlayerDeathEvent(PlayerDeathEvent event) {
        this.main.MODULE_MECHANICS.Handle(event);
        this.main.MODULE_DEATH.Handle(event);
    }

    /**
     * Entity Death Event Dispatching
     * @param event the entity death event
     */
    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        this.main.MODULE_MECHANICS.Handle(event);
    }
}
