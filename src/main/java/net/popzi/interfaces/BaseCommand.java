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

    /**
     * Provides the name of the command, used for lookup
     * @return String of the command name
     */
    String getName();

    /**
     * Provides a description of the command about what it does
     * @return String description
     */
    @SuppressWarnings("unused")
    String getDescription();

    /**
     * Provides how to use the command
     * @return String usage
     */
    @SuppressWarnings("unused")
    String getUsage();

    /**
     * Provides args supported by the command
     * @return List<String> of acceptable arguments
     */
    List<String> getArgs();

    /**
     * Provides the permission required to execute the command
     * @return String permission
     */
    @SuppressWarnings("unused")
    String getPermission();

    // Sub commands

    /**
     * Should ideally always return true, as to prevent echoing from bukkit
     * @param sender the player executing the command
     * @param args the args provided to the command by the player
     * @return boolean (typically true) when executed
     */
    boolean execute(CommandSender sender, String[] args);

    /**
     * An internal reference of all the subCommands assigned to this command.
     * Typically, this is used in the module constructor to register subcommands (I.e. /foo bar doSomething)
     */
    HashMap<String, BaseCommand> subCommands = new HashMap<>();

    /**
     * Provides a list of subCommands registered to this baseCommand
     * @return HasMap<String, BaseCommand> of subCommands
     */
    default HashMap<String, BaseCommand> getSubCommands() {
        return subCommands;
    }

    /**
     * Registered a subCommand to this command. When doing so it will add this to the subCommands hashMap.
     * @param parentClass A parent class to provide. Not currently used for anything other than logging purposes.
     * @param subCommand The subCommand (of baseCommand, or self, type) to register
     * @param <T> type of the parent class
     */
    default <T> void registerSubCommand(T parentClass, BaseCommand subCommand) {
        if (parentClass instanceof Main)
            ((Main) parentClass).LOGGER.log(Level.INFO, "Registered SubCommand: " + subCommand.getName().toLowerCase());
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }

    // Tab completion

    /**
     * Returns a filtered list of args from the getArgs() function
     * @param sender player requesting the tab completion
     * @param args applicable with the current filters
     * @return List<String> of available options
     */
    default List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

}
