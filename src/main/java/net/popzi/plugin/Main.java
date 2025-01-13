package net.popzi.plugin;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin implements Listener {

    public Config CFG;
    public boolean MODULE_DEATH;
    public boolean MODULE_TROLL;
    public boolean MODULE_EVENTS;
    public boolean MODULE_MECHANICS;
    public Logger LOGGER;
    public EventDispatcher DISPATCH;

    /**
     * Constructor, initial setup
     */
    public Main() {
        this.LOGGER = Logger.getLogger(this.getName());
        this.CFG = new Config(this);
        this.DISPATCH = new EventDispatcher(this);
    }

    /***
     * Override the Bukkit onEnable() method, which executes at server boot up.
     */
    @Override
    public void onEnable() {
        this.LOGGER.log(Level.INFO, "PopziNet Loaded");
        this.DISPATCH.Register();
    }

    /**
     * Override the Bukkit onDisable() method, which executes at server shutdown, or reload.
     */
    @Override
    public void onDisable() {
        this.LOGGER.log(Level.INFO, "PopziNet Unloaded.");
    }

}
