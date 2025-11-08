package net.popzi.modules.tours.commands;

import net.popzi.core.Serializer;
import net.popzi.interfaces.BaseCommand;
import net.popzi.modules.tours.Tours;
import net.popzi.modules.tours.sql.registerTour;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

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
        if (icon.isEmpty()) return true;
        if (args.length != 2) return true;

        // Insert
        boolean registered = registerTour.register(
                this.tours.main,
                args[0],
                sendee.getLocation().getWorld().getUID().toString(),
                sendee.getLocation().getBlockX(),
                sendee.getLocation().getBlockY(),
                sendee.getLocation().getBlockZ(),
                Integer.parseInt(args[1]),
                Serializer.itemToBase64(icon)
        );

        // Inform
        sendee.sendMessage(args[0] + " registered: " + registered);
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
