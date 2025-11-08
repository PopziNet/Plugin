package net.popzi.modules.tours.sql;

import net.popzi.Main;
import net.popzi.records.Tour;
import net.popzi.sql.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Level;

public class getTours {
    public static List<Tour> get(Main main) {
        try (
            Connection c = main.DB.connect()) {
            PreparedStatement s = c.prepareStatement("SELECT * FROM Tours;");
            ResultSet r = s.executeQuery();
            return SQL.map(r, Tour.class);
        } catch (Exception e) {
            main.LOGGER.log(Level.WARNING, "Failed to retrieve from database", e);
            return null;
        }
    }
}
