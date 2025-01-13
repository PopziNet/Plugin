package net.popzi.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import java.util.logging.Level;

/***
 * Our EventDispatcher class has 1 purpose: Listen for events, and dispatch actions within the plugin.
 * No actionable code should *actually* run here.
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
        this.main.LOGGER.log(Level.INFO, "Lol someone died");
    }


}
