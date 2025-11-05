package net.popzi.modules.tours.commands;

import net.popzi.Main;
import net.popzi.interfaces.BaseCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class RegisterTour implements BaseCommand {

    Main main;

    public RegisterTour(Main main) {
        this.main = main;
    }

    @Override
    public String getName() {
        return "Register";
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
    public List<String> getArgs() {
        return List.of("<name>", "<distance>");
    }

    @Override
    public String getPermission() {
        return "pn.tour.register";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        int index = args.length - 1;
        if (index < 0 || index >= this.getArgs().size())
            return Collections.emptyList();
        return Collections.singletonList(this.getArgs().get(index));
    }
}
