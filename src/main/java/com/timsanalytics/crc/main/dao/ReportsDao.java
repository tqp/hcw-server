package com.timsanalytics.crc.main.dao;

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

    public List<SummaryReportResult> getCaseManagerCoverageReport() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
//        query.append("      student_id AS id,\n");
//        query.append("      CONCAT(given_name, ' ', surname) AS text\n");
//        query.append("  FROM\n");
//        query.append("  -- ROOT QUERY\n");
//        query.append("  (\n");
//        query.append(getActiveStudents_RootQuery());
//        query.append("  ) AS ROOT_QUERY\n");
//        query.append("  -- END ROOT QUERY\n");
//        query.append("  ORDER BY\n");
//        query.append("      surname,\n");
//        query.append("      given_name\n");
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                SummaryReportResult row = new SummaryReportResult();
                row.setId(rs.getInt("id"));
                row.setText(rs.getString("text"));
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
