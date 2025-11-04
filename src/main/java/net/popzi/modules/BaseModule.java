package net.popzi.modules;

import net.popzi.Main;
import net.popzi.interfaces.BaseCommand;
import net.popzi.interfaces.Module;

import java.util.HashMap;
import java.util.logging.Level;

public abstract class BaseModule implements Module {

    private final HashMap<String, BaseCommand> cmds = new HashMap<>();
    public Main main;

    public BaseModule(Main main) {
        this.main = main;
    }

    @Override
    public HashMap<String, BaseCommand> getCommands() {
        return this.cmds;
    }

    @Override
    public void registerCommand(BaseCommand cmd) {
        this.main.getLogger().log(Level.INFO, "Registered Command: " + cmd.getName());
        this.cmds.put(cmd.getName(), cmd);
    }

    @Override
    public BaseCommand getCommand(String name) {
        return this.cmds.get(name);
    }

    @Override
    public void unregisterCommand(String cmd) {
        this.cmds.remove(cmd);
    }
}
