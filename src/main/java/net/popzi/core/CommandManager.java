package net.popzi.core;

import net.popzi.Main;
import net.popzi.interfaces.BaseCommand;
import net.popzi.interfaces.Module;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Level;


public class CommandManager implements CommandExecutor, TabCompleter {

    private final Main main;
    private final CommandNode root = new CommandNode("");

    /**
     * Constructor
     * @param main instance of the plugin
     */
    public CommandManager(Main main) {
        this.main = main;
        this.initializeCommandHooks();
        root
            .addChild("death")
                .addChild("search")
                .addChild("restore")
                .getParent()
                .getParent()
            .addChild("event")
                .addChild("start");

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
        this.main.LOGGER.log(Level.INFO, "Command Label: " + command.getLabel());
        this.main.LOGGER.log(Level.INFO, "Command Name: " + command.getName());
        this.main.LOGGER.log(Level.INFO, "String: " + s);
        this.main.LOGGER.log(Level.INFO, "Args[]: " + Arrays.toString(args));

        Module tours = this.main.MODULE_MANAGER.getModule("MODULE_TOURS");
        if (tours == null)
            return false;

        return tours.getCommand("Tour").execute(commandSender, args);
    }

    /**
     * The onTabComplete handler
     * @param commandSender The player or entity or server executing the command
     * @param command The command its self ( />>pn<< foo bar 123 )
     * @param s The command as a string, for some reason ( />>pn<< foo bar 123 )
     * @param strings The command arguments as an array ( /pn >>foo bar 123<< )
     * @return List<String> of possible options
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        CommandNode node = root;

        // Traverse to the node that matches the command path so far
        for (int i = 0; i < strings.length - 1; i++) {
            CommandNode next = node.getChild(strings[i]);
            if (next == null) return Collections.emptyList();
            node = next;
        }

        String currentArg = strings[strings.length - 1].toLowerCase();
        List<String> completions = new ArrayList<>();

        for (String childName : node.getChildrenNames()) {
            if (childName.toLowerCase().startsWith(currentArg)) {
                completions.add(childName);
            }
        }

        return completions;
    }
}
