package net.popzi.modules.tours;

import net.popzi.Main;
import net.popzi.modules.BaseModule;
import net.popzi.modules.tours.commands.RegisterTour;
import net.popzi.modules.tours.commands.Tour;
import net.popzi.modules.tours.commands.UnregisterTour;
import org.bukkit.event.Event;


public class Tours extends BaseModule {

    /**
     * Constructor
     * @param main plugin instance
     */
    public Tours(Main main) {
        super(main);
        this.registerCommand(new RegisterTour(this.main));
        this.registerCommand(new RegisterTour(this.main));
        this.registerCommand(new UnregisterTour(this.main));
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
