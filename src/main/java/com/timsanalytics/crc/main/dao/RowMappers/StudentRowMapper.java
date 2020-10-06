package com.timsanalytics.crc.main.dao.RowMappers;

import com.timsanalytics.crc.main.beans.Student;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRowMapper implements RowMapper<Student> {

    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student row = new Student();
        row.setStudentId(rs.getInt("id"));
        row.setStudentSurname(rs.getString("last_name"));
        row.setStudentGivenName(rs.getString("first_name"));
        row.setStudentGender(rs.getString("gender"));
        row.setStudentDateOfBirth(rs.getString("dob"));
        row.setStatus(rs.getString("deleted"));
        return row;
    }
}
