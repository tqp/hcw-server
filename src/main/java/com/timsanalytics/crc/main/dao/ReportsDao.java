package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.main.beans.Student;
import com.timsanalytics.crc.main.beans.SummaryReportResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportsDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;

    @Autowired
    public ReportsDao(JdbcTemplate mySqlAuthJdbcTemplate) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
    }

    public List<Student> getCaseManagerCoverageReport() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Person_Student.student_id,\n");
        query.append("      Person_Student.surname,\n");
        query.append("      Person_Student.given_name,\n");
        query.append("      Person_Student.dob\n");
        query.append("  FROM\n");
        query.append("      CRC.Person_Student\n");
        query.append("      LEFT JOIN CRC.Rel_Student_Case_Manager ON Rel_Student_Case_Manager.student_id = Person_Student.student_id\n");
        query.append("  WHERE\n");
        query.append("      Rel_Student_Case_Manager.student_id IS NULL\n");
        query.append("  ORDER BY\n");
        query.append("      Person_Student.surname,\n");
        query.append("      Person_Student.given_name\n");
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                Student row = new Student();
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("surname"));
                row.setStudentGivenName(rs.getString("given_name"));
                row.setStudentDateOfBirth(rs.getString("dob"));
                return row;
            });
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public List<Student> getCaregiverCoverageReport() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Person_Student.student_id,\n");
        query.append("      Person_Student.surname,\n");
        query.append("      Person_Student.given_name,\n");
        query.append("      Person_Student.dob\n");
        query.append("  FROM\n");
        query.append("      CRC.Person_Student\n");
        query.append("      LEFT JOIN CRC.Rel_Student_Caregiver ON Rel_Student_Caregiver.student_id = Person_Student.student_id\n");
        query.append("  WHERE\n");
        query.append("      Rel_Student_Caregiver.student_id IS NULL\n");
        query.append("  ORDER BY\n");
        query.append("      Person_Student.surname,\n");
        query.append("      Person_Student.given_name\n");
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                Student row = new Student();
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("surname"));
                row.setStudentGivenName(rs.getString("given_name"));
                row.setStudentDateOfBirth(rs.getString("dob"));
                return row;
            });
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }
}
