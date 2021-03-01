package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.main.beans.Workshop;
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
public class WorkshopDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;
    private final PrintObjectService printObjectService;

    @Autowired
    public WorkshopDao(JdbcTemplate mySqlAuthJdbcTemplate, PrintObjectService printObjectService) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.printObjectService = printObjectService;
    }

    public Workshop createWorkshopEntry(String username, Workshop workshop) {
        this.printObjectService.PrintObject("workshop", workshop);
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
                        ps.setInt(1, workshop.getCaregiverId());
                        ps.setString(2, workshop.getWorkshopName());
                        ps.setString(3, workshop.getWorkshopDate());
                        ps.setString(4, username);
                        ps.setString(5, username);
                        return ps;
                    }
            );
            return workshop;
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
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
