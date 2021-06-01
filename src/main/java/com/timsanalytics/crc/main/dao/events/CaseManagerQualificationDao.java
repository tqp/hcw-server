package com.timsanalytics.crc.main.dao.events;

import com.timsanalytics.crc.common.beans.KeyValueInteger;
import com.timsanalytics.crc.main.beans.CaseManagerQualification;
import com.timsanalytics.crc.utils.PrintObjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class CaseManagerQualificationDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;

    @Autowired
    public CaseManagerQualificationDao(JdbcTemplate mySqlAuthJdbcTemplate) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
    }

    public CaseManagerQualification createWorkshopEntry(String username, CaseManagerQualification caseManagerQualification) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.Rel_Case_Manager_Qualification\n");
        query.append("      (\n");
        query.append("          case_manager_id,\n");
        query.append("          qualification_institution,\n");
        query.append("          qualification_name,\n");
        query.append("          created_on,\n");
        query.append("          created_by,\n");
        query.append("          updated_on,\n");
        query.append("          updated_by,\n");
        query.append("          deleted\n");
        query.append("      )\n");
        query.append("      VALUES\n");
        query.append("      (\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          now(),\n");
        query.append("          ?,\n");
        query.append("          now(),\n");
        query.append("          ?,\n");
        query.append("          0\n");
        query.append("      )\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, caseManagerQualification.getCaseManagerId());
                        ps.setString(2, caseManagerQualification.getQualificationInstitution());
                        ps.setString(3, caseManagerQualification.getQualificationName());
                        ps.setString(4, username);
                        ps.setString(5, username);
                        return ps;
                    }
            );
            return caseManagerQualification;
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public CaseManagerQualification getCaseManagerQualificationDetail(Integer caseManagerQualificationId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      case_manager_qualification_id,\n");
        query.append("      case_manager_id,\n");
        query.append("      qualification_institution,\n");
        query.append("      qualification_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Rel_Case_Manager_Qualification\n");
        query.append("  WHERE\n");
        query.append("      case_manager_qualification_id = ?\n");
        query.append("      AND deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      qualification_institution,\n");
        query.append("      qualification_name\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{caseManagerQualificationId}, (rs, rowNum) -> {
                CaseManagerQualification row = new CaseManagerQualification();
                row.setCaseManagerQualificationId(rs.getInt("case_manager_qualification_id"));
                row.setCaseManagerId(rs.getInt("case_manager_id"));
                row.setQualificationInstitution(rs.getString("qualification_institution"));
                row.setQualificationName(rs.getString("qualification_name"));
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

    public CaseManagerQualification updateCaseManagerQualification(CaseManagerQualification caseManagerQualification) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Rel_Case_Manager_Qualification\n");
        query.append("  SET\n");
        query.append("      qualification_institution = ?,\n");
        query.append("      qualification_name = ?\n");
        query.append("  WHERE\n");
        query.append("      case_manager_qualification_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, caseManagerQualification.getQualificationInstitution());
                        ps.setString(2, caseManagerQualification.getQualificationName());
                        ps.setInt(3, caseManagerQualification.getCaseManagerQualificationId());
                        return ps;
                    }
            );
            return this.getCaseManagerQualificationDetail(caseManagerQualification.getCaseManagerQualificationId());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public KeyValueInteger deleteCaseManagerQualification(int caseManagerQualificationId) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Rel_Case_Manager_Qualification\n");
        query.append("  SET\n");
        query.append("      deleted = 1\n");
        query.append("  WHERE\n");
        query.append("      case_manager_qualification_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        this.logger.trace("CaseManagerQualificationId=" + caseManagerQualificationId);
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, caseManagerQualificationId);
                        return ps;
                    }
            );
            return new KeyValueInteger("caseManagerQualificationId", caseManagerQualificationId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    // JOINED TABLES

    public List<CaseManagerQualification> getQualificationListByCaseManagerId(int caseManagerId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      case_manager_qualification_id,\n");
        query.append("      qualification_institution,\n");
        query.append("      qualification_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Rel_Case_Manager_Qualification\n");
        query.append("  WHERE\n");
        query.append("      case_manager_id = ?\n");
        query.append("      AND deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      qualification_name DESC\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{caseManagerId}, (rs, rowNum) -> {
                CaseManagerQualification row = new CaseManagerQualification();
                row.setCaseManagerQualificationId(rs.getInt("case_manager_qualification_id"));
                row.setQualificationInstitution(rs.getString("qualification_institution"));
                row.setQualificationName(rs.getString("qualification_name"));
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
