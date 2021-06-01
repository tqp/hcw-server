package com.timsanalytics.crc.main.dao.events;

import com.timsanalytics.crc.common.beans.KeyValueInteger;
import com.timsanalytics.crc.main.beans.CaregiverWorkshop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class CaregiverWorkshopDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;

    @Autowired
    public CaregiverWorkshopDao(JdbcTemplate mySqlAuthJdbcTemplate) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
    }

    public CaregiverWorkshop createWorkshopEntry(String username, CaregiverWorkshop caregiverWorkshop) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.Rel_Caregiver_Workshop\n");
        query.append("      (\n");
        query.append("          caregiver_id,\n");
        query.append("          workshop_name,\n");
        query.append("          workshop_date,\n");
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
                        ps.setInt(1, caregiverWorkshop.getCaregiverId());
                        ps.setString(2, caregiverWorkshop.getWorkshopName());
                        ps.setString(3, caregiverWorkshop.getWorkshopDate());
                        ps.setString(4, username);
                        ps.setString(5, username);
                        return ps;
                    }
            );
            return caregiverWorkshop;
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public CaregiverWorkshop getCaregiverWorkshopDetail(Integer caregiverWorkshopId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      caregiver_workshop_id,\n");
        query.append("      caregiver_id,\n");
        query.append("      workshop_name,\n");
        query.append("      workshop_date\n");
        query.append("  FROM\n");
        query.append("      CRC.Rel_Caregiver_Workshop\n");
        query.append("  WHERE\n");
        query.append("      caregiver_workshop_id = ?\n");
        query.append("      AND deleted = 0\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{caregiverWorkshopId}, (rs, rowNum) -> {
                CaregiverWorkshop row = new CaregiverWorkshop();
                row.setCaregiverWorkshopId(rs.getInt("caregiver_workshop_id"));
                row.setCaregiverId(rs.getInt("caregiver_id"));
                row.setWorkshopName(rs.getString("workshop_name"));
                row.setWorkshopDate(rs.getString("workshop_date"));
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

    public CaregiverWorkshop updateCaregiverWorkshop(CaregiverWorkshop caregiverWorkshop) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Rel_Caregiver_Workshop\n");
        query.append("  SET\n");
        query.append("      workshop_name = ?,\n");
        query.append("      workshop_date = ?\n");
        query.append("  WHERE\n");
        query.append("      caregiver_workshop_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, caregiverWorkshop.getWorkshopName());
                        ps.setString(2, caregiverWorkshop.getWorkshopDate());
                        ps.setInt(3, caregiverWorkshop.getCaregiverWorkshopId());
                        return ps;
                    }
            );
            return this.getCaregiverWorkshopDetail(caregiverWorkshop.getCaregiverWorkshopId());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public KeyValueInteger deleteCaregiverWorkshop(int caregiverWorkshopId) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Rel_Caregiver_Workshop\n");
        query.append("  SET\n");
        query.append("      deleted = 1\n");
        query.append("  WHERE\n");
        query.append("      caregiver_workshop_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        this.logger.trace("caregiverWorkshopId=" + caregiverWorkshopId);
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, caregiverWorkshopId);
                        return ps;
                    }
            );
            return new KeyValueInteger("caregiverWorkshopId", caregiverWorkshopId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    // JOINED TABLES

    public List<CaregiverWorkshop> getWorkshopListByCaregiverId(int caregiverId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      caregiver_workshop_id,\n");
        query.append("      workshop_name,\n");
        query.append("      workshop_date\n");
        query.append("  FROM\n");
        query.append("      CRC.Rel_Caregiver_Workshop\n");
        query.append("  WHERE\n");
        query.append("      caregiver_id = ?\n");
        query.append("      AND deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      workshop_date DESC\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{caregiverId}, (rs, rowNum) -> {
                CaregiverWorkshop row = new CaregiverWorkshop();
                row.setCaregiverWorkshopId(rs.getInt("caregiver_workshop_id"));
                row.setWorkshopName(rs.getString("workshop_name"));
                row.setWorkshopDate(rs.getString("workshop_date"));
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
