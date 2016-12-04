package net.catten.ssim.helper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by catten on 11/14/16.
 */
public class TableMaker {

    private Properties tableProperty;
    private Connection connection;
    private Logger logger;
    private String prefix;

    public TableMaker(Connection connection, Properties tableProperty, String prefix) {
        this.connection = connection;
        this.tableProperty = tableProperty;
        this.prefix = prefix;
        logger = Logger.getLogger(this.getClass().toString());
    }

    public void make(List<String> makeList) throws SQLException {
        Statement statement = connection.createStatement();
        for (String s : makeList) {
            int result = statement.executeUpdate(String.format("CREATE TABLE %s (%s)", prefix + s,tableProperty.get(s)));
            logger.info(String.format("Created table %s, result = %d", prefix + s, result));
        }
    }

    public Properties getTableProperty() {
        return tableProperty;
    }

    public void setTableProperty(Properties tableProperty) {
        this.tableProperty = tableProperty;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
