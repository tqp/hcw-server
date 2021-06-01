package com.timsanalytics.crc.main.dao.events;

import com.timsanalytics.crc.main.beans.StudentSponsorLetter;
import com.timsanalytics.crc.main.dao.UtilsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentSponsorLetterDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;
    private final UtilsDao utilsDao;

    @Autowired
    public StudentSponsorLetterDao(JdbcTemplate mySqlAuthJdbcTemplate, UtilsDao utilsDao) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.utilsDao = utilsDao;
    }

    // BASIC CRUD

    // JOINED TABLES

    public List<StudentSponsorLetter> getStudentSponsorLetterListByStudentId(Integer studentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      sponsor_letter_id,\n");
        query.append("      student_id,\n");
        query.append("      Person_Sponsor.sponsor_id,\n");
        query.append("      Person_Sponsor.surname,\n");
        query.append("      Person_Sponsor.given_name,\n");
        query.append("      sponsor_letter_date\n");
        query.append("  FROM\n");
        query.append("      CRC.Rel_Student_Sponsor_Letter\n");
        query.append("      LEFT JOIN CRC.Person_Sponsor ON Person_Sponsor.sponsor_id = Rel_Student_Sponsor_Letter.sponsor_id AND Person_Sponsor.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      student_id = ?\n");
        query.append("      AND Rel_Student_Sponsor_Letter.deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      sponsor_letter_date DESC\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{studentId}, (rs, rowNum) -> {
                StudentSponsorLetter row = new StudentSponsorLetter();
                row.setStudentSponsorLetterId(rs.getInt("sponsor_letter_id"));
                row.setStudentId(rs.getInt("student_id"));
                row.setSponsorId(rs.getString("sponsor_id"));
                row.setSponsorSurname(rs.getString("surname"));
                row.setSponsorGivenName(rs.getString("given_name"));
                row.setStudentSponsorLetterDate(rs.getString("sponsor_letter_date"));
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

    public List<StudentSponsorLetter> getStudentSponsorLetterListBySponsorId(Integer studentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      sponsor_letter_id,\n");
        query.append("      sponsor_id,\n");
        query.append("      Person_Student.student_id,\n");
        query.append("      Person_Student.surname,\n");
        query.append("      Person_Student.given_name,\n");
        query.append("      sponsor_letter_date\n");
        query.append("  FROM\n");
        query.append("      CRC.Rel_Student_Sponsor_Letter\n");
        query.append("      LEFT JOIN CRC.Person_Student ON Person_Student.student_id = Rel_Student_Sponsor_Letter.student_id AND Person_Student.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      sponsor_id = ?\n");
        query.append("      AND Rel_Student_Sponsor_Letter.deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      sponsor_letter_date DESC\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{studentId}, (rs, rowNum) -> {
                StudentSponsorLetter row = new StudentSponsorLetter();
                row.setStudentSponsorLetterId(rs.getInt("sponsor_letter_id"));
                row.setSponsorId(rs.getString("sponsor_id"));
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("surname"));
                row.setStudentGivenName(rs.getString("given_name"));
                row.setStudentSponsorLetterDate(rs.getString("sponsor_letter_date"));
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
