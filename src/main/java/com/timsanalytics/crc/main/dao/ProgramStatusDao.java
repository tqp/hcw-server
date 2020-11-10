package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.main.beans.ProgramStatus;
import com.timsanalytics.crc.main.beans.ProgramStatusPackage;
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
        query.append("      program_status_type_id,\n");
        query.append("      name,\n");
        query.append("      child_field_title\n");
        query.append("  FROM\n");
        query.append("      CRC.Ref_Program_Status_Type\n");
        query.append("  WHERE\n");
        query.append("      program_status_type_id = ?\n");
        query.append("      AND deleted = 0\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{programStatusId}, (rs, rowNum) -> {
                ProgramStatusPackage row = new ProgramStatusPackage();
                row.setProgramStatusId(rs.getInt("program_status_type_id"));
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
        query.append("      program_status_type_id,\n");
        query.append("      name\n");
        query.append("  FROM\n");
        query.append("      CRC.Ref_Program_Status_Type\n");
        query.append("  WHERE\n");
        query.append("      parent_id = ?\n");
        query.append("      AND program_status_type_id != 0\n");
        query.append("      AND deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      name\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{parentId}, (rs, rowNum) -> {
                ProgramStatusPackage row = new ProgramStatusPackage();
                row.setProgramStatusId(rs.getInt("program_status_type_id"));
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
        query.append("      student_id,\n");
        query.append("      program_status_level_one_id,\n");
        query.append("      level_one.name AS program_status_level_one_name,\n");
        query.append("      program_status_level_two_id,\n");
        query.append("      level_two.name AS program_status_level_two_name,\n");
        query.append("      program_status_level_three_id,\n");
        query.append("      level_three.name AS program_status_level_three_name,\n");
        query.append("      start_date\n");
        query.append("  FROM\n");
        query.append("      CRC.Rel_Student_Program_Status\n");
        query.append("      LEFT JOIN CRC.Ref_Program_Status_Type level_one ON level_one.program_status_type_id = Rel_Student_Program_Status.program_status_level_one_id\n");
        query.append("      LEFT JOIN CRC.Ref_Program_Status_Type level_two ON level_two.program_status_type_id = Rel_Student_Program_Status.program_status_level_two_id\n");
        query.append("      LEFT JOIN CRC.Ref_Program_Status_Type level_three ON level_three.program_status_type_id = Rel_Student_Program_Status.program_status_level_three_id\n");
        query.append("  WHERE\n");
        query.append("      student_id = ?\n");
        query.append("      AND Rel_Student_Program_Status.deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      start_date DESC\n");
        query.append("  LIMIT 0, 1\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{studentId}, (rs, rowNum) -> {
                ProgramStatus row = new ProgramStatus();
                row.setStudentId(rs.getInt("student_id"));
                row.setProgramStatusLevelOneId(rs.getInt("program_status_level_one_id"));
                row.setProgramStatusLevelOneName(rs.getString("program_status_level_one_name"));
                row.setProgramStatusLevelTwoId(rs.getInt("program_status_level_two_id"));
                row.setProgramStatusLevelTwoName(rs.getString("program_status_level_two_name"));
                row.setProgramStatusLevelThreeId(rs.getInt("program_status_level_three_id"));
                row.setProgramStatusLevelThreeName(rs.getString("program_status_level_three_name"));
                row.setProgramStatusStartDate(rs.getString("start_date"));
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
