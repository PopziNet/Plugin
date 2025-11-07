package net.popzi.modules.tours;

import net.kyori.adventure.text.Component;
import net.popzi.Main;
import net.popzi.modules.BaseModule;
import net.popzi.modules.tours.commands.Tour;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.*;

import java.awt.*;


public class Tours extends BaseModule {

    public final Component GUI_TITLE;

    /**
     * Constructor
     * @param main plugin instance
     */
    public Tours(Main main) {
        super(main);
        this.GUI_TITLE = Component.text("Server Tours!");
        this.registerCommand(new Tour(this));
    }

    @Override
    public String getName() {
        return "MODULE_TOURS";
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof InventoryClickEvent)
            this.handleInventoryClick((InventoryClickEvent) event);
    }

    public void handleInventoryClick(InventoryClickEvent e) {

        if (e.getView().title().equals(this.GUI_TITLE))
            e.setCancelled(true);
    }

}
