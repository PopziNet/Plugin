package net.popzi.modules.tours;

import net.popzi.Main;
import net.popzi.modules.BaseModule;
import net.popzi.modules.tours.commands.Tour;
import org.bukkit.event.Event;


public class Tours extends BaseModule {

    /**
     * Constructor
     * @param main plugin instance
     */
    public Tours(Main main) {
        super(main);
        this.registerCommand(new Tour(this.main));
    }

    @Override
    public String getName() {
        return "MODULE_TOURS";
    }

    @Override
    public void handleEvent(Event event) {

    }
}
