package ca.jrvs.apps.jdbc;

import java.sql.*;
import java.util.Properties;

public class DatabaseConnectionManager {
//    central place for handling all connection needs
    private final String url;
    private final Properties properties;

    public DatabaseConnectionManager(String hostname, String databaseName, String username, String password) {
        this.url = "jdbc:postgresql://"+hostname+"/"+databaseName;
        this.properties = new Properties();
        this.properties.setProperty("user", username);
        this.properties.setProperty("password", password);
    }

    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(this.url, this.properties);
    }
}
