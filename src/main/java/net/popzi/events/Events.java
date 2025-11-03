package net.popzi.events;
import net.popzi.plugin.Main;
import net.popzi.plugin.ModuleManager.Module;
import org.bukkit.event.Event;



public class Events implements Module {

    private final Main main;

    /**
     * Constructor
     * @param main instance of the plugin
     */
    public Events(Main main) {
        this.main = main;
    }

    @Override
    public String getName() {
        return "MODULE_EVENTS";
    }

    @Override
    public void handleEvent(Event event) {
    }


}
