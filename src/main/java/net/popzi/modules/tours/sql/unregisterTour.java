package net.popzi.modules.tours.sql;

import net.popzi.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class unregisterTour {
    public static boolean unregister(Main main, String name) {
        try (Connection c = main.DB.connect()) {
            PreparedStatement statement = c.prepareStatement("DELETE FROM Tours WHERE name=?;");
            statement.setString(1, name);
            statement.execute();
            return true;
        } catch (SQLException e) {
            main.LOGGER.log(Level.WARNING, "Failed to remove from database", e);
            return false;
        }
    }
}
