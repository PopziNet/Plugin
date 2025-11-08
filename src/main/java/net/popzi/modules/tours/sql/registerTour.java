package net.popzi.modules.tours.sql;

import net.popzi.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class registerTour {
    public static boolean register(
            Main main,
            String name,
            String world,
            int X,
            int Y,
            int Z,
            int distance,
            String iconSerialized
    ) {
        try (Connection c = main.DB.connect()) {
            PreparedStatement statement = c.prepareStatement(
                    "INSERT INTO Tours (name, world, x, y, z, distance, iconItemStackB64) VALUES (?, ?, ?, ?, ?, ?, ?);"
            );
            statement.setString(1, name);
            statement.setString(2, world);
            statement.setInt(3, X);
            statement.setInt(4, Y);
            statement.setInt(5, Z);
            statement.setInt(6, distance);
            statement.setString(7, iconSerialized);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            main.LOGGER.log(Level.WARNING, "Failed to insert into database", e);
        }
        return false;
    }
}
