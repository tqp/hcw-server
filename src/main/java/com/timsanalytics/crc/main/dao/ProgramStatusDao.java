package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.main.beans.ProgramStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramStatusDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;

    @Autowired
    public ProgramStatusDao(JdbcTemplate mySqlAuthJdbcTemplate) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
    }

    public List<ProgramStatus> getProgramStatusList() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      program_status_id,\n");
        query.append("      name,\n");
        query.append("      parent_id,\n");
        query.append("      has_children\n");
        query.append("  FROM\n");
        query.append("      CRC.ProgramStatus\n");
        query.append("  WHERE\n");
        query.append("      parent_id = 0\n");
        query.append("      AND deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      name\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                ProgramStatus row = new ProgramStatus();
                row.setProgramStatusId(rs.getInt("program_status_id"));
                row.setProgramStatusName(rs.getString("name"));
                row.setHasChildren(rs.getInt("has_children") == 1);
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

    public List<ProgramStatus> getProgramStatusTopLevelList() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      program_status_id,\n");
        query.append("      name,\n");
        query.append("      parent_id,\n");
        query.append("      has_children\n");
        query.append("  FROM\n");
        query.append("      CRC.ProgramStatus\n");
        query.append("  WHERE\n");
        query.append("      parent_id = 0\n");
        query.append("      AND deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      name\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                ProgramStatus row = new ProgramStatus();
                row.setProgramStatusId(rs.getInt("program_status_id"));
                row.setProgramStatusName(rs.getString("name"));
                row.setHasChildren(rs.getInt("has_children") == 1);
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

    public List<ProgramStatus> getProgramStatusChildList(Integer parentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      program_status_id,\n");
        query.append("      name,\n");
        query.append("      parent_id,\n");
        query.append("      has_children\n");
        query.append("  FROM\n");
        query.append("      CRC.ProgramStatus\n");
        query.append("  WHERE\n");
        query.append("      parent_id = ?\n");
        query.append("      AND deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      name\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{parentId}, (rs, rowNum) -> {
                ProgramStatus row = new ProgramStatus();
                row.setProgramStatusId(rs.getInt("program_status_id"));
                row.setProgramStatusName(rs.getString("name"));
                row.setHasChildren(rs.getInt("has_children") == 1);
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
