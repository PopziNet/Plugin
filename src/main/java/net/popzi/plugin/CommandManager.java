package net.popzi.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;


public class CommandManager implements CommandExecutor, TabCompleter {

    private final Main main;
    private final CommandNode root = new CommandNode("");

    public CommandManager(Main main) {
        this.main = main;
        PluginCommand cmd = this.main.getCommand("pn");
        root
            .addChild("death")
                .addChild("search")
                .addChild("restore")
                .getParent()
                .getParent()
            .addChild("event")
                .addChild("start");

        if (cmd != null)
            cmd.setExecutor(this);

    }
    /**
     * The onCommand handler
     * @param commandSender The player or entity or server executing the command
     * @param command The command its self ( />>pn<< foo bar 123 )
     * @param s The command as a string, for some reason ( />>pn<< foo bar 123 )
     * @param strings The command arguments as an array ( /pn >>foo bar 123<< )
     * @return boolean
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        this.main.LOGGER.log(Level.INFO, "Sender: " + commandSender.getName());
        this.main.LOGGER.log(Level.INFO, "Command: " + command.getLabel() + " Name: " + command.getName());
        this.main.LOGGER.log(Level.INFO, "String: " + s);
        this.main.LOGGER.log(Level.INFO, "StringS: " + Arrays.toString(strings));

        return false;
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
