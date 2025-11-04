package net.popzi.interfaces;

import org.bukkit.command.CommandSender;

/**
 * Defines what a BaseCommand is. Any package anywhere in the project can define a BaseCommand.
 * A BaseCommand can be registered to this command manager by using CommandManager.register(BaseCommand cmd);
 */
public interface BaseCommand {
    String getName();

    String getDescription();

    String getUsage();

    String getPermission();

    boolean execute(CommandSender sender, String[] args);
}
