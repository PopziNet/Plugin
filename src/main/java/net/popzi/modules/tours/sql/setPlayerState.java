package net.popzi.modules.tours.sql;

import net.popzi.Main;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;

public class setPlayerState {

    public static boolean set(Main main, Player player, int tourID) {
        try (Connection c = main.DB.connect()) {
            String UUID = player.getUniqueId().toString();
            String world = player.getLocation().getWorld().getUID().toString();
            int X = player.getLocation().getBlockX();
            int Y = player.getLocation().getBlockY();
            int Z = player.getLocation().getBlockZ();

            PreparedStatement statement = c.prepareStatement(
                    "INSERT INTO ToursPlayers (UUID, world, x, y, z, tourID) VALUES (?, ?, ?, ?, ?, ?);"
            );
            statement.setString(1, UUID);
            statement.setString(2, world);
            statement.setInt(3, X);
            statement.setInt(4, Y);
            statement.setInt(5, Z);
            statement.setInt(6, tourID);

            statement.execute();
            return true;
        } catch (Exception e) {
            main.LOGGER.log(Level.WARNING, "Could not set player state", e);
            return false;
        }
    }

}
