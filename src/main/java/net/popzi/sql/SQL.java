package net.popzi.sql;

import net.popzi.Main;
import org.codehaus.plexus.util.IOUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    /**
     * Converts a ResultSet to a List<T> of a Record type
     * @apiNote Example: SQL.map(rs, net.popzi.records.Tour.class)
     * @param rs the ResultSet returned from the database
     * @param type the class of the Record to convert to (E.g. Car.class)
     * @return List<T> of that Record type
     * @param <T> the Record class
     * @throws Exception if it's unable to convert
     */
    public static <T> List<T> map(ResultSet rs, Class<T> type) throws Exception {
        List<T> list = new ArrayList<>();
        var fields = type.getDeclaredFields();

        while (rs.next()) {
            Object[] args = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                Object value = rs.getObject(field.getName());

                // Handle nulls for primitive int fields
                if (value == null && field.getType().equals(int.class))
                    value = 0;

                args[i] = value;
            }
            list.add(
                    type.getDeclaredConstructor(Arrays.stream(fields)
                            .map(Field::getType)
                            .toArray(Class[]::new))
                    .newInstance(args));
        }

        return list;
    }
}
