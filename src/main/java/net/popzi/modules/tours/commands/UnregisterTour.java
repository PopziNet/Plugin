package net.popzi.modules.tours.commands;

import net.popzi.Main;
import net.popzi.interfaces.BaseCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class UnregisterTour implements BaseCommand {
    Main main;

    public UnregisterTour(Main main) {
        this.main = main;
    }

    @Override
    public String getName() {
        return "Unregister";
    }

    @Override
    public String getDescription() {
        return "Removes a tour from the list of available tours.";
    }

    @Override
    public String getUsage() {
        return "/tour unregister <name>";
    }

    @Override
    public List<String> getArgs() {
        return List.of("<name>");
    }

    @Override
    public String getPermission() {
        return "pn.tour.unregister";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        return false;
    }
}
