package net.catten.ssim.helper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by catten on 11/13/16.
 */
public class TableChecker {
    private Connection connection;
    private ArrayList<String> tables;
    private String prefix;

    public TableChecker(Connection connection, ArrayList<String> tables, String prefix) {
        this.connection = connection;
        this.tables = tables;
        this.prefix = prefix;
    }

    public List<String> checkNotExist() throws SQLException {
        List<String> results = new LinkedList<>();
        for (String s : tables) {
            ResultSet resultSet = connection.getMetaData().getTables(null, connection.getSchema(), prefix + s, new String[]{"TABLE"});
            if (!resultSet.next()) results.add(s);
        }
        return results;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<String> getTables() {
        return tables;
    }

    public void setTables(ArrayList<String> tables) {
        this.tables = tables;
    }
}
