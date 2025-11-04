package net.popzi.modules.events;
import net.popzi.Main;
import net.popzi.modules.BaseModule;
import org.bukkit.event.Event;



public class Events extends BaseModule {

    /**
     * Constructor
     * @param main instance of the plugin
     */
    public Events(Main main) {
        super(main);
    }

    @Override
    public String getName() {
        return "MODULE_EVENTS";
    }

    @Override
    public void handleEvent(Event event) {
    }


}
