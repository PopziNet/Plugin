package net.popzi;

import net.popzi.modules.death.Death;
import net.popzi.modules.events.Events;
import net.popzi.modules.mechanics.Mechanics;
import net.popzi.core.CommandManager;
import net.popzi.core.Config;
import net.popzi.core.EventDispatcher;
import net.popzi.core.ModuleManager;
import net.popzi.modules.tips.Tips;
import net.popzi.modules.tours.Tours;
import net.popzi.sql.SQL;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin implements Listener {

    public Config CFG;
    public ModuleManager MODULE_MANAGER;
    public Logger LOGGER;
    public EventDispatcher DISPATCH;
    public SQL DB;
    public CommandManager COMMAND_MANAGER;

    /**
     * Constructor, initial setup.
     * Things that don't require bukkit should be put here.
     */
    public Main() {
        this.LOGGER = Logger.getLogger(this.getName());
        this.DISPATCH = new EventDispatcher(this);
    }

    /***
     * Override the Bukkit onEnable() method, which executes at server boot up.
     * Things that require bukkit should be put here.
     */
    @Override
    public void onEnable() {
        this.CFG = new Config(this);
        this.DB = new SQL(this);
        this.COMMAND_MANAGER = new CommandManager(this);
        this.MODULE_MANAGER = new ModuleManager(this);
        this.MODULE_MANAGER.registerModule(new Mechanics(this));
        this.MODULE_MANAGER.registerModule(new Death(this));
        this.MODULE_MANAGER.registerModule(new Events(this));
        this.MODULE_MANAGER.registerModule(new Tours(this));
        this.MODULE_MANAGER.registerModule(new Tips(this));
        this.MODULE_MANAGER.printModules();
        this.DISPATCH.Register();
        this.LOGGER.log(Level.INFO, "PopziNet Loaded");
    }

    /**
     * Override the Bukkit onDisable() method, which executes at server shutdown, or reload.
     */
    @Override
    public void onDisable() {
        this.MODULE_MANAGER.stopEventProcessor();
        this.LOGGER.log(Level.INFO, "PopziNet Unloaded");
    }
}
