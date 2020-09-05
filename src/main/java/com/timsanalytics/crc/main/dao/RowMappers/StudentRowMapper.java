package com.timsanalytics.crc.main.dao.RowMappers;

import com.timsanalytics.crc.main.beans.Student;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRowMapper implements RowMapper<Student> {

    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student row = new Student();
        row.setStudentId(rs.getInt("PERSON_GUID"));
        row.setStudentSurname(rs.getString("PERSON_SURNAME"));
        row.setStudentGivenName(rs.getString("PERSON_GIVEN_NAME"));
        row.setStudentGender(rs.getString("PERSON_GENDER"));
        row.setStudentDateOfBirth(rs.getString("PERSON_DATE_OF_BIRTH"));
        row.setStatus(rs.getString("STATUS"));
        return row;
    }
}
