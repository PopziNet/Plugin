package net.popzi.modules.tours.sql;

import net.popzi.Main;
import net.popzi.records.ToursPlayers;
import net.popzi.sql.SQL;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.logging.Level;

public class getPlayerState {

    public static ToursPlayers get(Main main, Player player) {
        try (Connection c = main.DB.connect()) {

            PreparedStatement s = c.prepareStatement(
                    "SELECT * FROM ToursPlayers WHERE UUID=?;"
            );
            s.setString(1, player.getUniqueId().toString());
            List<ToursPlayers> re = SQL.map(s.executeQuery(), ToursPlayers.class);
            if (!re.isEmpty())
                return re.get(0);
            return null;

        } catch (Exception e) {
            main.LOGGER.log(Level.WARNING, "Failed to retrieve playerState from database", e);
            return null;
        }
    }
}
