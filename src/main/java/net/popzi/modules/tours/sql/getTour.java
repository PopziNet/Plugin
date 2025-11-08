package net.popzi.modules.tours.sql;

import net.popzi.Main;
import net.popzi.records.Tour;
import net.popzi.sql.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.logging.Level;

public class getTour {
    public static Tour get(Main main, int tourID) {
        try (Connection c = main.DB.connect()) {
            PreparedStatement p = c.prepareStatement("SELECT * FROM Tours where ID=?;");
            p.setInt(1, tourID);
            List<Tour> re = SQL.map(p.executeQuery(), Tour.class);
            if (!re.isEmpty())
                return re.get(0);
            return null;
        } catch (Exception e) {
            main.LOGGER.log(Level.WARNING, "Failed to get a tour", e);
            return null;
        }
    }
}
