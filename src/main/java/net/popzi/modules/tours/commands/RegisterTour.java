package net.popzi.modules.tours.commands;

import net.popzi.Main;
import net.popzi.core.Serializer;
import net.popzi.interfaces.BaseCommand;
import net.popzi.modules.tours.Tours;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class RegisterTour implements BaseCommand {

    Tours tours;

    public RegisterTour(Tours tours) {
        this.tours = tours;
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
        // Get the item in the players hand
        Player sendee = (Player) sender;
        ItemStack icon = sendee.getInventory().getItemInMainHand();
        if (icon.isEmpty()) return false;
        if (args.length != 2) return false;

        this.tours.main.LOGGER.log(Level.INFO, Arrays.toString(args));

        // Prepare for db
        String iconSerialized = Serializer.itemToBase64(icon);
        String name = args[0];
        String World = sendee.getLocation().getWorld().getUID().toString();
        int X = sendee.getLocation().getBlockX();
        int Y = sendee.getLocation().getBlockY();
        int Z = sendee.getLocation().getBlockZ();
        int distance = Integer.parseInt(args[1]);

        // Insert
        try (Connection c = this.tours.main.DB.connect()) {
            PreparedStatement statement = c.prepareStatement(
                    "INSERT INTO Tours (Name, World, X, Y, Z, Distance, IconItemStackB64) VALUES (?, ?, ?, ?, ?, ?, ?);"
            );
            statement.setString(1, name);
            statement.setString(2, World);
            statement.setInt(3, X);
            statement.setInt(4, Y);
            statement.setInt(5, Z);
            statement.setInt(6, distance);
            statement.setString(7, iconSerialized);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Inform
        sendee.sendMessage(name + " registered.");
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
