package com.timsanalytics.crc.main.dao.events;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.main.beans.SponsorLetter;
import com.timsanalytics.crc.main.dao.UtilsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class SponsorLetterDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;
    private final UtilsDao utilsDao;

    @Autowired
    public SponsorLetterDao(JdbcTemplate mySqlAuthJdbcTemplate, UtilsDao utilsDao) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.utilsDao = utilsDao;
    }

    // BASIC CRUD

    public SponsorLetter createSponsorLetter(SponsorLetter sponsorLetter) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.Rel_Student_Sponsor_Letter\n");
        query.append("      (\n");
        query.append("          student_id,\n");
        query.append("          sponsor_id,\n");
        query.append("          sponsor_letter_date,\n");
        query.append("          deleted\n");
        query.append("      )\n");
        query.append("      VALUES\n");
        query.append("      (\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          0\n");
        query.append("      )\n");
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, sponsorLetter.getStudentId());
                        ps.setInt(2, sponsorLetter.getSponsorId());
                        ps.setString(3, sponsorLetter.getSponsorLetterDate());
                        return ps;
                    }
            );
            int lastInsertId = this.utilsDao.getLastInsertId();
            this.logger.trace("New Post-Grad Event ID: " + lastInsertId);
            return this.getSponsorLetterDetail(lastInsertId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public List<SponsorLetter> getSponsorLetterList() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      sponsor_letter_id,\n");
        query.append("      Rel_Student_Sponsor_Letter.student_id,\n");
        query.append("      Person_Student.surname AS student_surname,\n");
        query.append("      Person_Student.given_name as student_given_name,\n");
        query.append("      Rel_Student_Sponsor_Letter.sponsor_id,\n");
        query.append("      Person_Sponsor.surname AS sponsor_surname,\n");
        query.append("      Person_Sponsor.given_name AS sponsor_given_name,\n");
        query.append("      sponsor_letter_date\n");
        query.append("  FROM\n");
        query.append("      CRC.Rel_Student_Sponsor_Letter\n");
        query.append("      LEFT JOIN CRC.Person_Student ON Person_Student.student_id = Rel_Student_Sponsor_Letter.student_id AND Person_Student.deleted = 0\n");
        query.append("      LEFT JOIN CRC.Person_Sponsor ON Person_Sponsor.sponsor_id = Rel_Student_Sponsor_Letter.sponsor_id AND Person_Sponsor.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      Rel_Student_Sponsor_Letter.deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      Person_Sponsor.surname,\n");
        query.append("      Person_Sponsor.given_name\n");
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                SponsorLetter row = new SponsorLetter();
                row.setSponsorLetterId(rs.getInt("sponsor_letter_id"));
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("student_surname"));
                row.setStudentGivenName(rs.getString("student_given_name"));
                row.setSponsorId(rs.getInt("sponsor_id"));
                row.setSponsorLetterDate(rs.getString("sponsor_letter_date"));
                row.setSponsorSurname(rs.getString("sponsor_surname"));
                row.setSponsorGivenName(rs.getString("sponsor_given_name"));
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

    public SponsorLetter getSponsorLetterDetail(int studentSponsorLetterId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      sponsor_letter_id,\n");
        query.append("      Rel_Student_Sponsor_Letter.student_id,\n");
        query.append("      Person_Student.surname AS student_surname,\n");
        query.append("      Person_Student.given_name as student_given_name,\n");
        query.append("      Rel_Student_Sponsor_Letter.sponsor_id,\n");
        query.append("      Person_Sponsor.surname AS sponsor_surname,\n");
        query.append("      Person_Sponsor.given_name AS sponsor_given_name,\n");
        query.append("      sponsor_letter_date\n");
        query.append("  FROM\n");
        query.append("      CRC.Rel_Student_Sponsor_Letter\n");
        query.append("      LEFT JOIN CRC.Person_Student ON Person_Student.student_id = Rel_Student_Sponsor_Letter.student_id AND Person_Student.deleted = 0\n");
        query.append("      LEFT JOIN CRC.Person_Sponsor ON Person_Sponsor.sponsor_id = Rel_Student_Sponsor_Letter.sponsor_id AND Person_Sponsor.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      sponsor_letter_id = ?\n");
        query.append("      AND Rel_Student_Sponsor_Letter.deleted = 0\n");
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{studentSponsorLetterId}, (rs, rowNum) -> {
                SponsorLetter row = new SponsorLetter();
                row.setSponsorLetterId(rs.getInt("sponsor_letter_id"));
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("student_surname"));
                row.setStudentGivenName(rs.getString("student_given_name"));
                row.setSponsorId(rs.getInt("sponsor_id"));
                row.setSponsorLetterDate(rs.getString("sponsor_letter_date"));
                row.setSponsorSurname(rs.getString("sponsor_surname"));
                row.setSponsorGivenName(rs.getString("sponsor_given_name"));
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

    public SponsorLetter updateSponsorLetter(SponsorLetter sponsorLetter) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Rel_Student_Sponsor_Letter\n");
        query.append("  SET\n");
        query.append("      student_id = ?,\n");
        query.append("      sponsor_id = ?,\n");
        query.append("      sponsor_letter_date = ?\n");
        query.append("  WHERE\n");
        query.append("      sponsor_letter_id = ?\n");
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, sponsorLetter.getStudentId());
                        ps.setInt(2, sponsorLetter.getSponsorId());
                        ps.setString(3, sponsorLetter.getSponsorLetterDate());
                        ps.setInt(4, sponsorLetter.getSponsorLetterId());
                        return ps;
                    }
            );
            return this.getSponsorLetterDetail(sponsorLetter.getSponsorLetterId());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public KeyValue deleteSponsorLetter(String sponsorLetterId) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Rel_Student_Sponsor_Letter\n");
        query.append("  SET\n");
        query.append("      deleted = 1\n");
        query.append("  WHERE\n");
        query.append("      sponsor_letter_id = ?\n");
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, sponsorLetterId);
                        return ps;
                    }
            );
            return new KeyValue("sponsorLetterId", sponsorLetterId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    // JOINED TABLES

    public List<SponsorLetter> getSponsorLetterListByStudentId(Integer studentId) {
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
                SponsorLetter row = new SponsorLetter();
                row.setSponsorLetterId(rs.getInt("sponsor_letter_id"));
                row.setStudentId(rs.getInt("student_id"));
                row.setSponsorId(rs.getInt("sponsor_id"));
                row.setSponsorSurname(rs.getString("surname"));
                row.setSponsorGivenName(rs.getString("given_name"));
                row.setSponsorLetterDate(rs.getString("sponsor_letter_date"));
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

    public List<SponsorLetter> getSponsorLetterListBySponsorId(Integer studentId) {
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
                SponsorLetter row = new SponsorLetter();
                row.setSponsorLetterId(rs.getInt("sponsor_letter_id"));
                row.setSponsorId(rs.getInt("sponsor_id"));
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("surname"));
                row.setStudentGivenName(rs.getString("given_name"));
                row.setSponsorLetterDate(rs.getString("sponsor_letter_date"));
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
