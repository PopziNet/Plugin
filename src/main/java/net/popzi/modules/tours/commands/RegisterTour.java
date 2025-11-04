package net.popzi.modules.tours.commands;

import net.popzi.Main;
import net.popzi.interfaces.BaseCommand;
import org.bukkit.command.CommandSender;

public class RegisterTour implements BaseCommand {

    Main main;

    public RegisterTour(Main main) {
        this.main = main;
    }

    @Override
    public String getName() {
        return "RegisterTour";
    }

    @Override
    public String getDescription() {
        return "Registers a location as a place players can tour in spectator mode. " +
                "Uses the item in your hand as the icon in the tour menu.";
    }

    @Override
    public String getUsage() {
        return "/tour register <name> <distance>";
    }

    @Override
    public String getPermission() {
        return "pn.tour.register";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        return false;
    }
}
