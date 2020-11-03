package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.main.beans.ProgramStatus;
import com.timsanalytics.crc.main.beans.ProgramStatusPackage;
import com.timsanalytics.crc.main.beans.Sponsor;
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

    public ProgramStatusPackage getProgramStatusPackage(Integer programStatusId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      program_status_id,\n");
        query.append("      name,\n");
        query.append("      child_field_title\n");
        query.append("  FROM\n");
        query.append("      CRC.Ref_Program_Status\n");
        query.append("  WHERE\n");
        query.append("      program_status_id = ?\n");
        query.append("      AND deleted = 0\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{programStatusId}, (rs, rowNum) -> {
                ProgramStatusPackage row = new ProgramStatusPackage();
                row.setProgramStatusId(rs.getInt("program_status_id"));
                row.setProgramStatusName(rs.getString("name"));
                row.setChildFieldTitle(rs.getString("child_field_title"));
                return row;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public List<ProgramStatusPackage> getProgramStatusChildList(Integer parentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      program_status_id,\n");
        query.append("      name\n");
        query.append("  FROM\n");
        query.append("      CRC.Ref_Program_Status\n");
        query.append("  WHERE\n");
        query.append("      parent_id = ?\n");
        query.append("      AND program_status_id != 0\n");
        query.append("      AND deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      name\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{parentId}, (rs, rowNum) -> {
                ProgramStatusPackage row = new ProgramStatusPackage();
                row.setProgramStatusId(rs.getInt("program_status_id"));
                row.setProgramStatusName(rs.getString("name"));
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

    // JOINED QUERIES

    public ProgramStatus getProgramStatusDetailByStudentId(int studentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Rel_Student_Program_Status.student_program_status_id,\n");
        query.append("      start_date,\n");
        query.append("      level_one.name AS level_one_status,\n");
        query.append("      level_two.name AS level_two_status,\n");
        query.append("      level_three.name AS level_three_status\n");
        query.append("  FROM\n");
        query.append("      CRC.Rel_Student_Program_Status\n");
        query.append("      LEFT JOIN CRC.Ref_Program_Status level_one ON level_one.program_status_id = Rel_Student_Program_Status.program_status_level_one\n");
        query.append("      LEFT JOIN CRC.Ref_Program_Status level_two ON level_two.program_status_id = Rel_Student_Program_Status.program_status_level_two\n");
        query.append("      LEFT JOIN CRC.Ref_Program_Status level_three ON level_three.program_status_id = Rel_Student_Program_Status.program_status_level_three\n");
        query.append("  WHERE\n");
        query.append("      student_id = ?\n");
        query.append("  ORDER BY\n");
        query.append("      start_date DESC\n");
        query.append("  LIMIT 0, 1\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{studentId}, (rs, rowNum) -> {
                ProgramStatus row = new ProgramStatus();
                row.setProgramStudentStatusId(rs.getString("student_program_status_id"));
                row.setProgramStatusStartDate(rs.getString("start_date"));
                row.setProgramStatusLevelOne(rs.getString("level_one_status"));
                row.setProgramStatusLevelTwo(rs.getString("level_two_status"));
                row.setProgramStatusLevelThree(rs.getString("level_three_status"));
                return row;
            });
        } catch (EmptyResultDataAccessException e) {
            return new ProgramStatus();
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

}
