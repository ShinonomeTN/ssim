package net.catten.ssim.repository;

import net.catten.ssim.schedule.model.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by catten on 11/14/16.
 */
public class SubjectRepository extends BaseRepository<Subject>{

    public SubjectRepository(Connection connection, String table) {
        super(connection,table);
    }

    @Override
    public void save(Subject entity) throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO " + table +"subjects (id,term,name,code,unit,timeSpend,point) VALUES (?,?,?,?,?,?,?)");
        preparedStatement.setInt(1,entity.getId());
        preparedStatement.setString(2,entity.getTerm());
        preparedStatement.setString(3,entity.getName());
        preparedStatement.setString(4,entity.getCode());
        preparedStatement.setString(5,entity.getUnit());
        preparedStatement.setDouble(6,entity.getTimeSpend());
        preparedStatement.setDouble(7,entity.getPoint());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

}
