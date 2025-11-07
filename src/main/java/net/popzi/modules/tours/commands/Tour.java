package net.popzi.modules.tours.commands;

import net.kyori.adventure.text.Component;
import net.popzi.Main;
import net.popzi.core.Serializer;
import net.popzi.interfaces.BaseCommand;
import net.popzi.sql.SQL;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.*;
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

        // Create inventory
        Inventory navigationInventory = Bukkit.createInventory(null, 54, Component.text("Server Tours!"));
        Player senderPlayer = (Player) sender;

        // Setup menu icons
        int iPrev = 45; // Also acts as a limit for where data must end.
        int iNext = 53;
        ItemStack itmPrev = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemStack itmNext = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta itmMPrev = itmPrev.getItemMeta();
        ItemMeta itmMNext = itmNext.getItemMeta();
        itmMPrev.displayName(Component.text("Previous"));
        itmMNext.displayName(Component.text("Next"));
        itmPrev.setItemMeta(itmMPrev);
        itmNext.setItemMeta(itmMNext);
        navigationInventory.setItem(iPrev, itmPrev);
        navigationInventory.setItem(iNext, itmNext);

        // Obtain data
        try (Connection c = this.main.DB.connect()) {
            PreparedStatement s = c.prepareStatement("SELECT * FROM Tours;");
            ResultSet r = s.executeQuery();
            List<net.popzi.records.Tour> re = SQL.map(r, net.popzi.records.Tour.class);

            // Map data, TODO: Page Handling
            re.forEach(tour -> {
                ItemStack itmTour = Serializer.itemFromBase64(tour.IconItemStackB64());
                navigationInventory.setItem(re.indexOf(tour), itmTour);
            });

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // Showtime
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
