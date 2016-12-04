package net.catten.ssim.repository;

import net.catten.ssim.dto.LessonDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by catten on 11/14/16.
 */
public class LessonRepository extends BaseRepository<LessonDTO> {

    public LessonRepository(Connection connection, String table) {
        super(connection,table);
    }

    @Override
    public void save(LessonDTO entity) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO " + table +" (subject,teacher,classNumber,attendAmount,classAttend,classType,assessmentType,week,timePoint,address) VALUE (?,?,?,?,?,?,?,?,?,?)");
        preparedStatement.setInt(1,entity.getSubject());
        preparedStatement.setString(2,entity.getTeacher());
        preparedStatement.setString(3,entity.getClassNumber());
        preparedStatement.setInt(4,entity.getAttendAmount());
        preparedStatement.setString(5,entity.getClassAttend());
        preparedStatement.setString(6,entity.getClassType());
        preparedStatement.setString(7,entity.getAssessmentType());
        preparedStatement.setInt(8,entity.getWeek());
        preparedStatement.setString(9,entity.getTimePoint());
        preparedStatement.setString(10,entity.getAddress());
        preparedStatement.execute();
        preparedStatement.close();
    }
}
