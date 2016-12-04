package net.catten.ssim.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by catten on 11/14/16.
 */
public class DatabaseConnector {
    private Properties properties;
    private Connection connection;

    /**
     * Simple database manager, use database property to connect database
     *
     * @param properties a property object contained :
     *                   hostname for target database server with port number
     *                   username for login to database
     *                   password for the database user
     *                   database for target database(schema)
     */
    public DatabaseConnector(Properties properties) throws ClassNotFoundException {
        this.properties = properties;
        Class.forName("com.mysql.jdbc.Driver");
    }

    /**
     * Get a database connection
     *
     * If connection doesn't exist, try to create a connection
     *
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        if(connection == null || connection.isClosed()){
            createConnection();
        }
        return connection;
    }

    /**
     * Close the connection
     *
     * @throws SQLException
     */
    public void closeConnection() throws SQLException {
        connection.close();
    }

    private void createConnection() throws SQLException {
        String connectString = String.format("jdbc:mysql://%s/%s?user=%s&password=%s&useUnicode=true&characterEncoding=utf8",
                properties.get("hostname"),
                properties.get("database"),
                properties.get("username"),
                properties.get("password"));
        connection = DriverManager.getConnection(connectString);
    }
}
