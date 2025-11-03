package net.popzi.plugin;

import org.bukkit.event.Event;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

/**
 * The ModuleManger class is responsible to registering, unregistering, activating and de-activating any
 * internal modules, and pushes any events and other operations to modules as needed.
 * Additionally, the manager starts a queue on another thread, such that any events that occur
 * faster than the plugin can handle, they are queued in a separate thread as to not impact server MPT.
 */
public class ModuleManager {

    private final Map<Module, Boolean> modules;
    private final Main main;
    private final BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>();
    private Thread eventThread;

    /**
     * The interface/type for what a module is
     * and the methods it should contain
     */
    public interface Module {
        String getName();
        void handleEvent(Event event);
    }

    /**
     * Constructor for the module manager. Note that the queue thread is started when we
     * register our event dispatcher.
     * @param main Main plugin instance
     */
    public ModuleManager(Main main) {
        this.modules = new ConcurrentHashMap<>();
        this.main = main;
    }

    /**
     * Registers modules to the module manager. Sets the state to active if `true` in the config.
     * @param module to register
     */
    public void registerModule(Module module) {
        if (module == null) throw new IllegalArgumentException("Module cannot be null");
        boolean active = this.main.CFG.getData().getBoolean(module.getName());
        modules.put(module, false);
        if (active) {
            modules.put(module, true);
        }
        this.main.LOGGER.log(Level.INFO, "Module registered: " + module.getName() + ", active: " + active);
    }

    /**
     * Unregisters modules to the module manager
     * @param module to register
     */
    @SuppressWarnings("unused")
    public void unregisterModule(Module module) {
        if (module == null) throw new IllegalArgumentException("Module cannot be null");
        modules.remove(module);
        this.main.LOGGER.log(Level.INFO, "Module unregistered: " + module.getName());
    }

    /**
     * Sets a registered module to be active
     * @param module to set
     */
    @SuppressWarnings("unused")
    public void activateModule(Module module) {
        if (module == null) throw new IllegalArgumentException("Module cannot be null");
        if (modules.containsKey(module)) {
            modules.put(module, true);
            this.main.LOGGER.log(Level.INFO, "Module activated: " + module.getName());
        } else {
            this.main.LOGGER.log(Level.INFO, "Module not found: " + module.getName());
        }
    }

    /**
     * Sets a registered module to be inactive.
     * @param module to set
     */
    @SuppressWarnings("unused")
    public void deactivateModule(Module module) {
        if (module == null) throw new IllegalArgumentException("Module cannot be null");
        if (modules.containsKey(module)) {
            modules.put(module, false);
            this.main.LOGGER.log(Level.INFO, "Module deactivated: " + module.getName());
        } else {
            this.main.LOGGER.log(Level.INFO, "Module not found: " + module.getName());
        }
    }

    /**
     * Prints out all of our modules and the state they're in. Useful for debugging.
     */
    @SuppressWarnings({"unused", "CodeBlock2Expr"})
    public void printModules() {
        modules.forEach((module, isActive) -> {
            this.main.LOGGER.log(Level.INFO, "Module: " + module.getName() + " Active: " + isActive);
        });
    }

    /**
     * Pushes server events to all modules so that they can be handled
     * @param event to handle
     */
    public void pushEvent(Event event) {
        // if (!event.getEventName().equalsIgnoreCase("ChunkLoadEvent"))
        //    this.main.LOGGER.log(Level.INFO, "Queueing event: " + event.getEventName());
        this.eventQueue.add(event);
    }

    /**
     * Creates a new thread where our queue is handled.
     * Not really needed if our plugin is small, but sensible to have.
     * Events get inserted via this.pushEvent() and processed by this looping thread.
     */
    public void startEventProcessor() {
        this.eventThread = new Thread(() -> {
            while (true) {
                try {
                    Event event = eventQueue.take(); // Waits for an event
                    //this.main.LOGGER.log(Level.INFO, "Processing event: " + event.getEventName());
                    modules.forEach((module, isActive) -> {
                        if (isActive) {
                            module.handleEvent(event);
                        }
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore the interrupted status
                    break;
                }
            }
        });
        this.eventThread.setDaemon(true);
        this.eventThread.start();
        if (this.eventThread.isAlive())
            this.main.LOGGER.log(Level.INFO, "Event queue thread started");
    }

    /**
     * Interrupts / kills the queue handling thread.
     * Best used when disabling the plugin.
     */
    public void stopEventProcessor() {
        while (!this.eventQueue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                this.main.LOGGER.log(Level.SEVERE, "Event queue thread forced close with pending jobs! May cause issues!");
            }
        }
        this.main.LOGGER.log(Level.INFO, "Event queue thread closed gracefully");
        this.eventThread.interrupt();
    }
}
