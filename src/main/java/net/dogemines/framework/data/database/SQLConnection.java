package net.dogemines.framework.data.database;

import net.dogemines.framework.DogeMinesFramework;

import java.sql.*;
import java.util.HashSet;

public class SQLConnection {
    private final String URL;
    private final Connection connection;

    public SQLConnection(String URL) {
        this.URL = URL;
        this.connection = connect();
    }

    private Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
