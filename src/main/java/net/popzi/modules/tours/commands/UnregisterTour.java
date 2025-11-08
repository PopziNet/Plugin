package net.popzi.modules.tours.commands;

import net.popzi.interfaces.BaseCommand;
import net.popzi.modules.tours.Tours;
import net.popzi.modules.tours.sql.unregisterTour;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
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
        Player sendee = (Player) sender;
        String name = args[0];
        boolean unregistered = unregisterTour.unregister(this.tours.main, name);
        // TODO: Remove anyone who may be on that tour
        //  (Not really needed since a reconnect will cover them, but nice to have)
        sendee.sendMessage(name + " removed: " + unregistered);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        int index = args.length - 1;
        if (index < 0 || index >= this.getArgs().size())
            return Collections.emptyList();
        return Collections.singletonList(this.getArgs().get(index));
    }
}
