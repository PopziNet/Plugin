package net.popzi.modules.death;

import net.popzi.Main;
import net.popzi.modules.BaseModule;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

public class Death extends BaseModule {

    /**
     * Constructor
     * @param main Main plugin
     */
    public Death(Main main) {
        super(main);
    }

    @Override
    public String getName() {
        return "MODULE_DEATH";
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof PlayerDeathEvent) {
            this.HandleDeath((PlayerDeathEvent) event);
        }
    }

    /**
     * Handles player death events
     * @param event the player death event
     */
    public void HandleDeath(PlayerDeathEvent event) {
        this.SQLInsertDeath(event);
    }

    /**
     * Handles logging player deaths into the database
     * @param event the player death event
     */
    public void SQLInsertDeath(PlayerDeathEvent event) {
        // Deconstruct all event variables into required formats
        String UUID = event.getEntity().getUniqueId().toString();
        String World = event.getEntity().getLocation().getWorld().getUID().toString();
        int X = event.getEntity().getLocation().getBlockX();
        int Y = event.getEntity().getLocation().getBlockY();
        int Z = event.getEntity().getLocation().getBlockZ();
        String InventoryB64 = this.InventoryToString(event.getEntity().getInventory());

        if (!InventoryB64.isEmpty()) {
            try ( // Upload into database
                Connection c = this.main.DB.connect()
            ) {
                PreparedStatement statement = c.prepareStatement("INSERT INTO Deaths (UUID, World, X, Y, Z, Inventory) VALUES (?, ?, ?, ?, ?, ?);");
                statement.setString(1, UUID);
                statement.setString(2, World);
                statement.setInt(3, X);
                statement.setInt(4, Y);
                statement.setInt(5, Z);
                statement.setString(6, InventoryB64);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Takes an inventory and turns it into a base64 string
     * @param inv the inventory to encode
     * @return string as base64 bytes.
     */
    public String InventoryToString(Inventory inv) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        Base64.Encoder encoder = Base64.getEncoder();
        for (ItemStack Item : inv.getContents()) {
            if (Item != null) {
                try {
                    byteStream.write(Item.serializeAsBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return encoder.encodeToString(byteStream.toByteArray());
    }
}
