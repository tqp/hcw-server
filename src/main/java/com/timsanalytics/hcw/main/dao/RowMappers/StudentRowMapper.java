package com.timsanalytics.hcw.main.dao.RowMappers;

import com.timsanalytics.hcw.main.beans.Student;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRowMapper implements RowMapper<Student> {

    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student row = new Student();
        row.setStudentGuid(rs.getString("PERSON_GUID"));
        row.setStudentSurname(rs.getString("PERSON_SURNAME"));
        row.setStudentGivenName(rs.getString("PERSON_GIVEN_NAME"));
        row.setStudentGender(rs.getString("PERSON_GENDER"));
        row.setStatus(rs.getString("STATUS"));
        return row;
    }
}
