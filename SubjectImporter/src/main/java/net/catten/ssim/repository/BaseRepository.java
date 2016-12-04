package net.catten.ssim.repository;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by catten on 11/14/16.
 */
public abstract class BaseRepository<T> {
    protected Connection connection;
    protected String table;

    public BaseRepository(Connection connection, String table) {
        this.connection = connection;
        this.table = table;
    }

    public abstract void save(T entity) throws SQLException;
}
