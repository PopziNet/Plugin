package net.popzi.modules.tours.commands;

import net.popzi.Main;
import net.popzi.interfaces.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class Tour implements BaseCommand {
    Main main;

    public Tour(Main main) {
        this.main = main;
        this.registerSubCommand(main, new RegisterTour(main));
        this.registerSubCommand(main, new UnregisterTour(main));
    }

    @Override
    public String getName() {
        return "Tour";
    }

    @Override
    public String getDescription() {
        return "Opens the tour navigation menu";
    }

    @Override
    public String getUsage() {
        return "/tour";
    }

    @Override
    public List<String> getArgs() {
        return null;
    }

    @Override
    public String getPermission() {
        return "pn.tour";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Inventory navigationInventory = Bukkit.createInventory(null, 54);
        Player senderPlayer = (Player) sender;

        senderPlayer.openInventory(navigationInventory);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        // TODO: Potentially review if this is something that can be implementation default
        return this.getSubCommands().keySet().stream()
                .filter(s -> s.startsWith(args[0].toLowerCase()))
                .sorted()
                .toList();
    }
}
