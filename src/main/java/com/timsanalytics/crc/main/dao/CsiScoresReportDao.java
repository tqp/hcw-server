package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.main.beans.CsiScoresReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CsiScoresReportDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;
    private final UtilsDao utilsDao;

    @Autowired
    public CsiScoresReportDao(JdbcTemplate mySqlAuthJdbcTemplate, UtilsDao utilsDao) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.utilsDao = utilsDao;
    }

    public List<CsiScoresReport> getCsiScoresReportData() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      case_manager_user_id,\n");
        query.append("      surname,\n");
        query.append("      given_name,\n");
        query.append("      total_score,\n");
        query.append("      COUNT(total_score) AS count\n");
        query.append("  FROM\n");
        query.append("      (\n");
        query.append("          SELECT\n");
        query.append("              Student_Csi.case_manager_user_id,\n");
        query.append("              Person_Case_Manager.surname,\n");
        query.append("              Person_Case_Manager.given_name,\n");
        query.append("              (\n");
        query.append("                  csi_score_food_security +\n");
        query.append("                  csi_score_nutrition_and_growth +\n");
        query.append("                  csi_score_shelter +\n");
        query.append("                  csi_score_abuse_and_exploitation +\n");
        query.append("                  csi_score_legal_protection +\n");
        query.append("                  csi_score_wellness +\n");
        query.append("                  csi_score_health_care_services +\n");
        query.append("                  csi_score_emotional_health +\n");
        query.append("                  csi_score_social_behavior +\n");
        query.append("                  csi_score_performance +\n");
        query.append("                  csi_score_education_and_work\n");
        query.append("              ) AS total_score\n");
        query.append("          FROM\n");
        query.append("              CRC.Student_Csi\n");
        query.append("              LEFT JOIN CRC.Person_Case_Manager ON Person_Case_Manager.case_manager_user_id = Student_Csi.case_manager_user_id\n");
        query.append("          WHERE\n");
        query.append("              year(csi_date) = 2020\n");
        query.append("          ORDER BY\n");
        query.append("              Person_Case_Manager.surname,\n");
        query.append("              Person_Case_Manager.given_name\n");
        query.append("      ) AS SUB_QUERY\n");
        query.append("  GROUP BY\n");
        query.append("      case_manager_user_id,\n");
        query.append("      surname,\n");
        query.append("      given_name\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                CsiScoresReport row = new CsiScoresReport();
                row.setCaseManagerId(rs.getInt("case_manager_user_id"));
                row.setCaseManagerSurname(rs.getString("surname"));
                row.setCaseManagerGivenName(rs.getString("given_name"));
                row.setTotalScore(rs.getInt("total_score"));
                row.setCount(rs.getInt("count"));
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
