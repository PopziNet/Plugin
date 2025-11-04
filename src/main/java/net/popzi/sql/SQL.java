package net.popzi.sql;

import net.popzi.Main;
import org.codehaus.plexus.util.IOUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.*;

public class SQL {

    private Connection connection;
    private final String URI;

    public SQL(Main main) {
        File filename = new File(main.getDataFolder(), "PopziNet.db");
        this.URI = String.format("jdbc:sqlite:%s", filename);
        String schema = null;

        // Read a text file by moving every single byte from the HDD into memory, good lord!
        try (
            InputStream fs = main.getResource("schema.sql");
            StringWriter writer = new StringWriter()
        ) {
            if (fs != null) {
                IOUtil.copy(fs, writer, "UTF-8");
                schema = writer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        // Handle connections and schema check
        try (
            Connection c = DriverManager.getConnection(this.URI);
            Statement statement = c.createStatement()
        ) {
            this.connection = c; // Store for later use.
            statement.setQueryTimeout(15);
            statement.executeUpdate(schema);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(this.URI);
        }
        return connection;
    }
}
