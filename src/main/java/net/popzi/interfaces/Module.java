package net.popzi.interfaces;

import org.bukkit.event.Event;

import java.util.HashMap;

/**
 * The interface/type for what a module is
 * and the methods it should contain
 */
public interface Module {

    String getName();

    void handleEvent(Event event);

    HashMap<String, BaseCommand> getCommands();

    BaseCommand getCommand(String name);

    void registerCommand(BaseCommand cmd);

    void unregisterCommand(String cmd);
}
