package com.timsanalytics.hcw.main.dao.RowMappers;

import com.timsanalytics.hcw.main.beans.Student;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRowMapper implements RowMapper<Student> {

    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student row = new Student();
        row.setStudentGuid(rs.getString("STUDENT_GUID"));
        row.setStudentSurname(rs.getString("STUDENT_SURNAME"));
        row.setStudentGivenName(rs.getString("STUDENT_GIVEN_NAME"));
        row.setStudentGender(rs.getString("STUDENT_GENDER"));
        row.setStatus(rs.getString("STATUS"));
        return row;
    }
}
