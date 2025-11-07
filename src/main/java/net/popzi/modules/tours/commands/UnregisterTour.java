package net.popzi.modules.tours.commands;

import net.popzi.interfaces.BaseCommand;
import net.popzi.modules.tours.Tours;
import org.bukkit.command.CommandSender;

import java.util.List;

public class UnregisterTour implements BaseCommand {
    Tours tours;

    public UnregisterTour(Tours tours) {
        this.tours = tours;
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
