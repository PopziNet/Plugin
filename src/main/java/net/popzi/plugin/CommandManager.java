package net.popzi.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.logging.Level;

public class CommandManager implements CommandExecutor {

    private final Main main;

    public CommandManager(Main main) {
        this.main = main;
        PluginCommand cmd = this.main.getCommand("pn");

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
}
