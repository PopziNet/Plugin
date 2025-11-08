package net.popzi.modules.tours;

import net.kyori.adventure.text.Component;
import net.popzi.Main;
import net.popzi.core.Serializer;
import net.popzi.modules.BaseModule;
import net.popzi.modules.tours.commands.Tour;
import net.popzi.modules.tours.sql.*;
import net.popzi.records.ToursPlayers;
import net.popzi.utils.Boundary;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;


public class Tours extends BaseModule {

    public final Component GUI_TITLE;

    /**
     * Constructor
     * @param main plugin instance
     */
    public Tours(Main main) {
        super(main);
        this.GUI_TITLE = Component.text("Server Tours!");
        this.registerCommand(new Tour(this));
    }

    @Override
    public String getName() {
        return "MODULE_TOURS";
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof InventoryClickEvent)
            this.handleInventoryClick((InventoryClickEvent) event);
        if (event instanceof PlayerJoinEvent)
            this.handlePlayerJoin((PlayerJoinEvent) event);
        if (event instanceof PlayerMoveEvent)
            this.handlePlayerMove((PlayerMoveEvent) event);
    }

    public void handleInventoryClick(InventoryClickEvent event) {
        if (!event.getView().title().equals(this.GUI_TITLE)) return;

        // We ARE in our custom menu
        event.setCancelled(true);
        Inventory inv = event.getClickedInventory();
        if (inv != null) inv.close();

        // What icon did we just click
        ItemStack itmClicked = event.getCurrentItem();
        if (itmClicked == null) return;

        // Get data and find the record with this icon
        List<net.popzi.records.Tour> re = getTours.get(this.main);
        if (re == null) return;

        re.forEach(tour -> {
            ItemStack itmTour = Serializer.itemFromBase64(tour.iconItemStackB64());
            if (itmTour.equals(itmClicked)) {
                // Send the event
                Player player = (Player) event.getWhoClicked();
                this.enterTour(player, tour);
            }
        });
    }

    public void handlePlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.isOp()) return;

        // A player has logged in, they're not OP
        // Check if they have a playerState we need to adjust, and adjust it so.
        ToursPlayers playerState = getPlayerState.get(this.main, player);
        if (playerState != null)
            this.exitTour(player, playerState);
    }

    public void handlePlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Limit our event to only spectators who aren't OP.
        if (player.getGameMode() != GameMode.SPECTATOR) return;
        if (player.isOp()) return;

        // Only limited to people in spectator mode and who are actively on a tour
        ToursPlayers playerState = getPlayerState.get(this.main, player);
        if (playerState == null) return;
        if (playerState.tourID() == 0) return;

        // Obtain tour information
        net.popzi.records.Tour t = getTour.get(this.main, playerState.tourID());
        if (t == null) return;
        Location tLocation = new Location(
                this.main.getServer().getWorld(UUID.fromString(t.world())),
                t.x(),
                t.y(),
                t.z()
        );

        // Check boundary, if they go too far, exit the tour
        Boundary x = new Boundary(tLocation, t.distance());
        if (!x.isInside(player.getLocation())) {
            // Reason for doing it like this, is so that this event remains off the main thread, since there's
            // quite a lot of database calls and happening which we ideally don't want on main.
            this.main.getServer().getScheduler().runTask(this.main, () -> {
                player.teleport(tLocation);
                player.sendMessage("You went too far from the tour area. You exit with /tour exit");
            });
        }
    }


    /**
     * Removed a player from any and all tours, and removes any database entry for their state
     * @param player to remove from all tours
     * @param playerState of the player, which will be used to teleport them back, and removed from the database
     */
    public void exitTour(Player player, ToursPlayers playerState) {
        Location newLoc = new Location(
                this.main.getServer().getWorld(UUID.fromString(playerState.world())),
                playerState.x(),
                playerState.y(),
                playerState.z()
        );
        player.teleport(newLoc);
        player.setGameMode(GameMode.SURVIVAL);
        removePlayerState.remove(this.main, player);
    }

    /**
     * Enters the player into a tour
     * @param player of the player to enter the tour
     * @param tour for the player to tour
     */
    public void enterTour(Player player, net.popzi.records.Tour tour) {
        boolean stateSaved = setPlayerState.set(this.main, player, tour.ID());

        if (!stateSaved) {
            player.sendMessage("Could not save your state.");
            return;
        }

        player.teleport(new Location(
                this.main.getServer().getWorld(UUID.fromString(tour.world())),
                tour.x(),
                tour.y(),
                tour.z()
        ));
        player.setGameMode(GameMode.SPECTATOR);
    }


}
