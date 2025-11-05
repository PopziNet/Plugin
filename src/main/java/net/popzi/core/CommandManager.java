package net.popzi.core;

import net.popzi.Main;
import net.popzi.interfaces.BaseCommand;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Level;


public class CommandManager implements CommandExecutor, TabCompleter {

    private final Main main;

    /**
     * Constructor
     * @param main instance of the plugin
     */
    public CommandManager(Main main) {
        this.main = main;
        this.initializeCommandHooks();
    }

    /**
     * Initializes our command hooks to the bukkit instance, per our plugin.yml file.
     */
    public void initializeCommandHooks() {
        PluginCommand pn = this.main.getCommand("pn");
        if (pn != null)
            pn.setExecutor(this);

        PluginCommand tour = this.main.getCommand("tour");
        if (tour != null)
            tour.setExecutor(this);
    }

    /**
     * The onCommand handler
     * @param commandSender The player or entity or server executing the command
     * @param command The command its self ( />>pn<< foo bar 123 )
     * @param s The command as a string, for some reason ( />>pn<< foo bar 123 )
     * @param args The command arguments as an array ( /pn >>foo bar 123<< )
     * @return boolean
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        this.main.LOGGER.log(Level.INFO, "Sender: " + commandSender.getName());
        this.main.LOGGER.log(Level.INFO, "Command Label: " + command.getLabel().toLowerCase());
        this.main.LOGGER.log(Level.INFO, "Command Name: " + command.getName().toLowerCase());
        this.main.LOGGER.log(Level.INFO, "String: " + s);
        this.main.LOGGER.log(Level.INFO, "Args[]: " + Arrays.toString(args));

        HashMap<String, BaseCommand> commands = this.main.MODULE_MANAGER.getAllCommands();
        BaseCommand cmd = commands.get(command.getLabel().toLowerCase());

        // Null check
        if (cmd == null) {
            commandSender.sendMessage("That command doesn't exist, or the module is disabled.");
            return false;
        }

        // We are likely trying to run a subcommand
        // If so, execute with the first arg (this) omitted.
        if (args.length > 0) {
            BaseCommand subCmd = cmd.getSubCommands().get(args[0]);
            if (subCmd == null) {
                commandSender.sendMessage("That sub command doesn't exist for: " + cmd.getName().toLowerCase());
                return false;
            }
            return cmd.getSubCommands().get(args[0]).execute(commandSender, Arrays.copyOfRange(args, 1, args.length));
        }

        // Base Commands
        return cmd.execute(commandSender, args);
    }

    /**
     * The onTabComplete handler
     * @param commandSender The player or entity or server executing the command
     * @param command The command its self ( />>pn<< foo bar 123 )
     * @param s The command as a string, for some reason ( />>pn<< foo bar 123 )
     * @param args The command arguments as an array ( /pn >>foo bar 123<< )
     * @return List<String> of possible options
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        this.main.LOGGER.log(Level.INFO, "Sender: " + commandSender.getName());
        this.main.LOGGER.log(Level.INFO, "Command Label: " + command.getLabel().toLowerCase());
        this.main.LOGGER.log(Level.INFO, "Command Name: " + command.getName().toLowerCase());
        this.main.LOGGER.log(Level.INFO, "String: " + s);
        this.main.LOGGER.log(Level.INFO, "Args[]: " + Arrays.toString(args) + " (" + args.length + ")");

        HashMap<String, BaseCommand> commands = this.main.MODULE_MANAGER.getAllCommands();
        BaseCommand cmd = commands.get(command.getLabel().toLowerCase());

        // Null check
        if (cmd == null)
            return null;

        // We are likely trying to run a subcommand
        // If so, tabComplete with the first arg (this) omitted.
        if (args.length > 1) {
            BaseCommand subCmd = cmd.getSubCommands().get(args[0]);
            if (subCmd == null)
                return null;

            return cmd.getSubCommands().get(args[0]).tabComplete(commandSender, Arrays.copyOfRange(args, 1, args.length));
        }

        // Base Commands
        return cmd.tabComplete(commandSender, args);
    }
}
