package net.popzi.interfaces;

import net.popzi.Main;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

/**
 * Defines what a BaseCommand is. Any package anywhere in the project can define a BaseCommand.
 * A BaseCommand can be registered to this command manager by using CommandManager.register(BaseCommand cmd);
 */
public interface BaseCommand {

    // Base command

    String getName();

    @SuppressWarnings("unused")
    String getDescription();

    @SuppressWarnings("unused")
    String getUsage();

    List<String> getArgs();

    @SuppressWarnings("unused")
    String getPermission();

    // Sub commands

    boolean execute(CommandSender sender, String[] args);

    HashMap<String, BaseCommand> subCommands = new HashMap<>();

    default HashMap<String, BaseCommand> getSubCommands() {
        return subCommands;
    }

    default <T> void registerSubCommand(T parentClass, BaseCommand subCommand) {
        if (parentClass instanceof Main)
            ((Main) parentClass).LOGGER.log(Level.INFO, "Registered SubCommand: " + subCommand.getName().toLowerCase());
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }

    // Tab completion

    default List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

}
