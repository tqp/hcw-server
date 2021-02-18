package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.main.beans.Workshop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkshopDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;

    @Autowired
    public WorkshopDao(JdbcTemplate mySqlAuthJdbcTemplate) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
    }

    public List<Workshop> getWorkshopListByCaregiverId(int caregiverId) {
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
                Workshop row = new Workshop();
                row.setCaregiverWorkshopId(rs.getInt("caregiver_workshop_id"));
                row.setWorkshopName(rs.getString("workshop_name"));
                row.setWorkshopDate(rs.getDate("workshop_date"));
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
