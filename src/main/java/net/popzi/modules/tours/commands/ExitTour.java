package net.popzi.modules.tours.commands;

import net.popzi.interfaces.BaseCommand;
import net.popzi.modules.tours.Tours;
import net.popzi.modules.tours.sql.getPlayerState;
import net.popzi.records.ToursPlayers;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ExitTour implements BaseCommand {

    private final Tours tours;

    public ExitTour(Tours tours) {
        this.tours = tours;
    }

    @Override
    public String getName() {
        return "Exit";
    }

    @Override
    public String getDescription() {
        return "Allows a player to exit the current tour";
    }

    @Override
    public String getUsage() {
        return "/tour exit";
    }

    @Override
    public List<String> getArgs() {
        return List.of();
    }

    @Override
    public String getPermission() {
        return "pn.tour";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        ToursPlayers playerState = getPlayerState.get(this.tours.main, player);
        if (playerState != null)
            this.tours.exitTour(player, playerState);
        return true;
    }
}
