package net.popzi.modules.tours.sql;

import net.popzi.Main;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;

public class removePlayerState {

    public static boolean remove(Main main, Player player) {
        try (Connection c = main.DB.connect()) {
            PreparedStatement s = c.prepareStatement("DELETE FROM ToursPlayers WHERE UUID=?;");
            s.setString(1, player.getUniqueId().toString());
            s.execute();
            return true;
        } catch (Exception e) {
            main.LOGGER.log(Level.WARNING, "Failed to remove player state", e);
            return false;
        }
    }
}
