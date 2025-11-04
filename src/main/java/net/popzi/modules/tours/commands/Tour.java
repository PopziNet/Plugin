package net.popzi.modules.tours.commands;

import net.popzi.Main;
import net.popzi.interfaces.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Tour implements BaseCommand {
    Main main;

    public Tour(Main main) {
        this.main = main;
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
}
