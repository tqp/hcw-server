package com.timsanalytics.crc.main.dao.events;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.main.beans.Csi;
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
public class CsiDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;
    private final UtilsDao utilsDao;

    @Autowired
    public CsiDao(JdbcTemplate mySqlAuthJdbcTemplate, UtilsDao utilsDao) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.utilsDao = utilsDao;
    }

    // BASIC CRUD

    public Csi createCsi(Csi csi) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.Student_Csi\n");
        query.append("      (\n");
        query.append("          student_id,\n");
        query.append("          case_manager_user_id,\n");
        query.append("          csi_date,\n");
        query.append("          csi_services_provided,\n");
        query.append("          csi_comments,\n");
        query.append("          csi_score_food_security,\n");
        query.append("          csi_score_nutrition_and_growth,\n");
        query.append("          csi_score_shelter,\n");
        query.append("          csi_score_care,\n");
        query.append("          csi_score_abuse_and_exploitation,\n");
        query.append("          csi_score_legal_protection,\n");
        query.append("          csi_score_wellness,\n");
        query.append("          csi_score_health_care_services,\n");
        query.append("          csi_score_emotional_health,\n");
        query.append("          csi_score_social_behavior,\n");
        query.append("          csi_score_performance,\n");
        query.append("          csi_score_education_and_work,\n");
        query.append("          deleted\n");
        query.append("      )\n");
        query.append("      VALUES\n");
        query.append("      (\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          0\n");
        query.append("      )\n");
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, csi.getStudentId());
                        ps.setInt(2, csi.getCaseManagerUserId());
                        ps.setString(3, csi.getCsiDate());
                        ps.setString(4, csi.getCsiServicesProvided());
                        ps.setString(5, csi.getCsiComments());
                        ps.setInt(6, csi.getCsiScoreFoodSecurity());
                        ps.setInt(7, csi.getCsiScoreNutritionAndGrowth());
                        ps.setInt(8, csi.getCsiScoreShelter());
                        ps.setInt(9, csi.getCsiScoreCare());
                        ps.setInt(10, csi.getCsiScoreAbuseAndExploitation());
                        ps.setInt(11, csi.getCsiScoreLegalProtection());
                        ps.setInt(12, csi.getCsiScoreWellness());
                        ps.setInt(13, csi.getCsiScoreHealthCareServices());
                        ps.setInt(14, csi.getCsiScoreEmotionalHealth());
                        ps.setInt(15, csi.getCsiScoreSocialBehavior());
                        ps.setInt(16, csi.getCsiScorePerformance());
                        ps.setInt(17, csi.getCsiScoreEducationAndWork());
                        return ps;
                    }
            );
            int lastInsertId = this.utilsDao.getLastInsertId();
            this.logger.debug("New Csi ID: " + lastInsertId);
            return this.getCsiDetail(lastInsertId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public List<Csi> getCsiList() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      student_csi_id,\n");
        query.append("      student_id\n");
        query.append("  FROM\n");
        query.append("      CRC.Student_Csi\n");
        query.append("  WHERE\n");
        query.append("      deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      csi_date DESC\n");
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                Csi row = new Csi();
                row.setStudentCsiId(rs.getInt("student_csi_id"));
                row.setStudentId(rs.getInt("student_id"));
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

    public int getCsiList_SSP_TotalRecords(ServerSidePaginationRequest<Csi> serverSidePaginationRequest) {
        StringBuilder query = new StringBuilder();
        query.append("          SELECT\n");
        query.append("              COUNT(*)\n");
        query.append("          FROM\n");
        query.append("          -- ROOT QUERY\n");
        query.append("          (\n");
        query.append(getCsiList_SSP_RootQuery(serverSidePaginationRequest));
        query.append("          ) AS ROOT_QUERY\n");
        query.append("          -- END ROOT QUERY\n");
        try {
            Integer count = this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{}, Integer.class);
            return count == null ? 0 : count;
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return 0;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return 0;
        }
    }

    public List<Csi> getCsiList_SSP(ServerSidePaginationRequest<Csi> serverSidePaginationRequest) {
        int pageStart = (serverSidePaginationRequest.getPageIndex()) * serverSidePaginationRequest.getPageSize();
        int pageSize = serverSidePaginationRequest.getPageSize();

        String defaultSortField = "csi_date";
        String defaultSortDirection = "DESC";
        String sortColumn = serverSidePaginationRequest.getSortColumn() != null ? serverSidePaginationRequest.getSortColumn() : defaultSortField;
        String sortDirection = serverSidePaginationRequest.getSortDirection() != null ? serverSidePaginationRequest.getSortDirection() : defaultSortDirection;

        StringBuilder query = new StringBuilder();
        query.append("  -- PAGINATION QUERY\n");
        query.append("  SELECT\n");
        query.append("      FILTER_SORT_QUERY.*\n");
        query.append("  FROM\n");

        query.append("      -- FILTER/SORT QUERY\n");
        query.append("      (\n");
        query.append("          SELECT\n");
        query.append("              *\n");
        query.append("          FROM\n");

        query.append("          -- ROOT QUERY\n");
        query.append("          (\n");
        query.append(getCsiList_SSP_RootQuery(serverSidePaginationRequest));
        query.append("          ) AS ROOT_QUERY\n");
        query.append("          -- END ROOT QUERY\n");

        query.append("          ORDER BY\n");
        query.append(sortColumn).append(" ").append(sortDirection.toUpperCase()).append(",\n");
        query.append("              csi_date DESC\n");
        query.append("      ) AS FILTER_SORT_QUERY\n");
        query.append("      -- END FILTER/SORT QUERY\n");

        query.append("  LIMIT ?, ?\n");
        query.append("  -- END PAGINATION QUERY\n");

        this.logger.trace("pageStart=" + pageStart + ", pageSize=" + pageSize);

        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{
                    pageStart,
                    pageSize
            }, (rs, rowNum) -> {
                Csi row = new Csi();
                row.setStudentCsiId(rs.getInt("student_csi_id"));
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("student_surname"));
                row.setStudentGivenName(rs.getString("student_given_name"));
                row.setCsiDate(rs.getString("csi_date"));
                row.setCaseManagerUserId(rs.getInt("user_id"));
                row.setCaseManagerSurname(rs.getString("case_manager_surname"));
                row.setCaseManagerGivenName(rs.getString("case_manager_given_name"));
                row.setCsiComments(rs.getString("csi_comments"));
                return row;
            });
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            this.logger.error("pageStart=" + pageSize + ", pageSize=" + pageSize);
            return null;
        }
    }

    private String getCsiList_SSP_RootQuery(ServerSidePaginationRequest<Csi> serverSidePaginationRequest) {
        //noinspection StringBufferReplaceableByString
        StringBuilder query = new StringBuilder();
        query.append("              SELECT\n");
        query.append("                  student_csi_id,\n");
        query.append("                  csi_date,\n");
        query.append("                  Student_Csi.student_id,\n");
        query.append("                  Person_Student.surname AS student_surname,\n");
        query.append("                  Person_Student.given_name AS student_given_name,\n");
        query.append("                  Student_Csi.user_id,\n");
        query.append("                  Auth_User.surname AS case_manager_surname,\n");
        query.append("                  Auth_User.given_name AS case_manager_given_name,\n");
        query.append("                  csi_comments\n");
        query.append("              FROM\n");
        query.append("                  CRC.Student_Csi\n");
        query.append("                  LEFT JOIN CRC.Person_Student ON Person_Student.student_id = Student_Csi.student_id AND Person_Student.deleted = 0\n");
        query.append("                  LEFT JOIN CRC.Auth_User ON Auth_User.user_id = Student_Csi.user_id AND Auth_User.deleted = 0\n");
        query.append("              WHERE\n");
        query.append("              (\n");
        query.append("                  Student_Csi.deleted = 0\n");
        query.append("              )\n");
        return query.toString();
    }

    private String getCsiList_SSP_AdditionalWhereClause(ServerSidePaginationRequest<Csi> serverSidePaginationRequest) {
        StringBuilder whereClause = new StringBuilder();
        String nameFilter = serverSidePaginationRequest.getNameFilter() != null ? serverSidePaginationRequest.getNameFilter() : "";

        // NAME FILTER CLAUSE
        if (!"".equalsIgnoreCase(nameFilter)) {
            whereClause.append("                  (\n");
            whereClause.append("                    UPPER(Student_Csi.surname) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                    OR\n");
            whereClause.append("                    UPPER(Student_Csi.given_name) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                  )\n");
        } else {
            whereClause.append("                  (1=1)\n");
        }

        return whereClause.toString();
    }

    public Csi getCsiDetail(int csiId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      student_csi_id,\n");
        query.append("      csi_date,\n");
        query.append("      Student_Csi.student_id,\n");
        query.append("      Person_Student.surname AS student_surname,\n");
        query.append("      Person_Student.given_name AS student_given_name,\n");
        query.append("      Student_Csi.case_manager_user_id,\n");
        query.append("      Auth_User.surname AS case_manager_surname,\n");
        query.append("      Auth_User.given_name AS case_manager_given_name,\n");
        query.append("      csi_comments,\n");
        query.append("      csi_services_provided,\n");
        query.append("      csi_score_food_security,\n");
        query.append("      csi_score_nutrition_and_growth,\n");
        query.append("      csi_score_shelter,\n");
        query.append("      csi_score_care,\n");
        query.append("      csi_score_abuse_and_exploitation,\n");
        query.append("      csi_score_legal_protection,\n");
        query.append("      csi_score_wellness,\n");
        query.append("      csi_score_health_care_services,\n");
        query.append("      csi_score_emotional_health,\n");
        query.append("      csi_score_social_behavior,\n");
        query.append("      csi_score_performance,\n");
        query.append("      csi_score_education_and_work\n");
        query.append("  FROM\n");
        query.append("      CRC.Student_Csi\n");
        query.append("      LEFT JOIN CRC.Person_Student ON Person_Student.student_id = Student_Csi.student_id AND Person_Student.deleted = 0\n");
        query.append("      LEFT JOIN CRC.Auth_User ON Auth_User.user_id = Student_Csi.case_manager_user_id AND Auth_User.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      student_csi_id = ?\n");
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{csiId}, (rs, rowNum) -> {
                Csi row = new Csi();
                row.setStudentCsiId(rs.getInt("student_csi_id"));
                row.setCsiDate(rs.getString("csi_date"));
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("student_surname"));
                row.setStudentGivenName(rs.getString("student_given_name"));
                row.setCaseManagerUserId(rs.getInt("case_manager_user_id"));
                row.setCaseManagerSurname(rs.getString("case_manager_surname"));
                row.setCaseManagerGivenName(rs.getString("case_manager_given_name"));
                row.setCsiComments(rs.getString("csi_comments"));
                row.setCsiServicesProvided(rs.getString("csi_services_provided"));
                row.setCsiScoreFoodSecurity(rs.getInt("csi_score_food_security"));
                row.setCsiScoreNutritionAndGrowth(rs.getInt("csi_score_nutrition_and_growth"));
                row.setCsiScoreShelter(rs.getInt("csi_score_shelter"));
                row.setCsiScoreCare(rs.getInt("csi_score_care"));
                row.setCsiScoreAbuseAndExploitation(rs.getInt("csi_score_abuse_and_exploitation"));
                row.setCsiScoreLegalProtection(rs.getInt("csi_score_legal_protection"));
                row.setCsiScoreWellness(rs.getInt("csi_score_wellness"));
                row.setCsiScoreHealthCareServices(rs.getInt("csi_score_health_care_services"));
                row.setCsiScoreEmotionalHealth(rs.getInt("csi_score_emotional_health"));
                row.setCsiScoreSocialBehavior(rs.getInt("csi_score_social_behavior"));
                row.setCsiScorePerformance(rs.getInt("csi_score_performance"));
                row.setCsiScoreEducationAndWork(rs.getInt("csi_score_education_and_work"));
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

    public Csi updateCsi(Csi csi) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Student_Csi\n");
        query.append("  SET\n");
        query.append("      student_id = ?,\n");
        query.append("      case_manager_user_id = ?,\n");
        query.append("      csi_date = ?,\n");
        query.append("      csi_services_provided = ?,\n");
        query.append("      csi_comments = ?,\n");
        query.append("      csi_score_food_security = ?,\n");
        query.append("      csi_score_nutrition_and_growth = ?,\n");
        query.append("      csi_score_shelter = ?,\n");
        query.append("      csi_score_care = ?,\n");
        query.append("      csi_score_abuse_and_exploitation = ?,\n");
        query.append("      csi_score_legal_protection = ?,\n");
        query.append("      csi_score_wellness = ?,\n");
        query.append("      csi_score_health_care_services = ?,\n");
        query.append("      csi_score_emotional_health = ?,\n");
        query.append("      csi_score_social_behavior = ?,\n");
        query.append("      csi_score_performance = ?,\n");
        query.append("      csi_score_education_and_work = ?\n");
        query.append("  WHERE\n");
        query.append("      student_csi_id = ?\n");
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, csi.getStudentId());
                        ps.setInt(2, csi.getCaseManagerUserId());
                        ps.setString(3, csi.getCsiDate());
                        ps.setString(4, csi.getCsiServicesProvided());
                        ps.setString(5, csi.getCsiComments());
                        ps.setInt(6, csi.getCsiScoreFoodSecurity());
                        ps.setInt(7, csi.getCsiScoreNutritionAndGrowth());
                        ps.setInt(8, csi.getCsiScoreShelter());
                        ps.setInt(9, csi.getCsiScoreCare());
                        ps.setInt(10, csi.getCsiScoreAbuseAndExploitation());
                        ps.setInt(11, csi.getCsiScoreLegalProtection());
                        ps.setInt(12, csi.getCsiScoreWellness());
                        ps.setInt(13, csi.getCsiScoreHealthCareServices());
                        ps.setInt(14, csi.getCsiScoreEmotionalHealth());
                        ps.setInt(15, csi.getCsiScoreSocialBehavior());
                        ps.setInt(16, csi.getCsiScorePerformance());
                        ps.setInt(17, csi.getCsiScoreEducationAndWork());
                        ps.setInt(18, csi.getStudentCsiId());
                        return ps;
                    }
            );
            return this.getCsiDetail(csi.getStudentCsiId());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public KeyValue deleteCsi(String csiRecordId) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Student_Csi\n");
        query.append("  SET\n");
        query.append("      deleted = 1\n");
        query.append("  WHERE\n");
        query.append("      student_csi_id = ?\n");
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, csiRecordId);
                        return ps;
                    }
            );
            return new KeyValue("csiRecordId", csiRecordId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    // JOINED TABLES

    public List<Csi> getCsiListByStudentId(Integer studentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      student_csi_id,\n");
        query.append("      csi_date,\n");
        query.append("      Student_Csi.student_id,\n");
        query.append("      Person_Student.surname AS student_surname,\n");
        query.append("      Person_Student.given_name AS student_given_name,\n");
        query.append("      Auth_User.user_id AS user_id,\n");
        query.append("      Auth_User.surname AS case_manager_surname,\n");
        query.append("      Auth_User.given_name AS case_manager_given_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Student_Csi\n");
        query.append("      LEFT JOIN CRC.Person_Student ON Person_Student.student_id = Student_Csi.student_id AND Person_Student.deleted = 0\n");
        query.append("      LEFT JOIN CRC.Auth_User ON Auth_User.user_id = Student_Csi.case_manager_user_id AND Auth_User.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      Student_Csi.student_id = ?\n");
        query.append("      AND Student_Csi.deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      csi_date DESC\n");
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{studentId}, (rs, rowNum) -> {
                Csi row = new Csi();
                row.setStudentCsiId(rs.getInt("student_csi_id"));
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("student_surname"));
                row.setStudentGivenName(rs.getString("student_given_name"));
                row.setCsiDate(rs.getString("csi_date"));
                row.setCaseManagerUserId(rs.getInt("user_id"));
                row.setCaseManagerSurname(rs.getString("case_manager_surname"));
                row.setCaseManagerGivenName(rs.getString("case_manager_given_name"));
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

    public Csi getMostRecentCsiScoresByStudentId(Integer studentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      student_csi_id,\n");
        query.append("      student_id,\n");
        query.append("      case_manager_user_id,\n");
        query.append("      csi_date,\n");
        query.append("      csi_score_food_security,\n");
        query.append("      csi_score_nutrition_and_growth,\n");
        query.append("      csi_score_shelter,\n");
        query.append("      csi_score_care,\n");
        query.append("      csi_score_abuse_and_exploitation,\n");
        query.append("      csi_score_legal_protection,\n");
        query.append("      csi_score_wellness,\n");
        query.append("      csi_score_health_care_services,\n");
        query.append("      csi_score_emotional_health,\n");
        query.append("      csi_score_social_behavior,\n");
        query.append("      csi_score_performance,\n");
        query.append("      csi_score_education_and_work\n");
        query.append("  FROM\n");
        query.append("      CRC.Student_Csi\n");
        query.append("  WHERE\n");
        query.append("      student_id = ?\n");
        query.append("      AND deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      csi_date DESC\n");
        query.append("  LIMIT 1\n");
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{studentId}, (rs, rowNum) -> {
                Csi row = new Csi();
                row.setStudentCsiId(rs.getInt("student_csi_id"));
                row.setStudentId(rs.getInt("student_id"));
                row.setCaseManagerUserId(rs.getInt("case_manager_user_id"));
                row.setCsiDate(rs.getString("csi_date"));
                row.setCsiScoreFoodSecurity(rs.getInt("csi_score_food_security"));
                row.setCsiScoreNutritionAndGrowth(rs.getInt("csi_score_nutrition_and_growth"));
                row.setCsiScoreShelter(rs.getInt("csi_score_shelter"));
                row.setCsiScoreCare(rs.getInt("csi_score_care"));
                row.setCsiScoreAbuseAndExploitation(rs.getInt("csi_score_abuse_and_exploitation"));
                row.setCsiScoreLegalProtection(rs.getInt("csi_score_legal_protection"));
                row.setCsiScoreWellness(rs.getInt("csi_score_wellness"));
                row.setCsiScoreHealthCareServices(rs.getInt("csi_score_health_care_services"));
                row.setCsiScoreEmotionalHealth(rs.getInt("csi_score_emotional_health"));
                row.setCsiScoreSocialBehavior(rs.getInt("csi_score_social_behavior"));
                row.setCsiScorePerformance(rs.getInt("csi_score_performance"));
                row.setCsiScoreEducationAndWork(rs.getInt("csi_score_education_and_work"));
                return row;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public List<Csi> getCsiListByCaseManagerId(Integer caseManagerId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      student_csi_id,\n");
        query.append("      csi_date,\n");
        query.append("      Student_Csi.student_id,\n");
        query.append("      Person_Student.surname AS student_surname,\n");
        query.append("      Person_Student.given_name AS student_given_name,\n");
        query.append("      Student_Csi.user_id,\n");
        query.append("      Person_Case_Manager.surname AS case_manager_surname,\n");
        query.append("      Person_Case_Manager.given_name AS case_manager_given_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Student_Csi\n");
        query.append("      LEFT JOIN CRC.Person_Student ON Person_Student.student_id = Student_Csi.student_id AND Person_Student.deleted = 0\n");
        query.append("      LEFT JOIN CRC.Person_Case_Manager ON Person_Case_Manager.user_id = Student_Csi.user_id AND Person_Case_Manager.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      Student_Csi.user_id = ?\n");
        query.append("      AND Student_Csi.deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      csi_date DESC\n");
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{caseManagerId}, (rs, rowNum) -> {
                Csi row = new Csi();
                row.setStudentCsiId(rs.getInt("student_csi_id"));
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("student_surname"));
                row.setStudentGivenName(rs.getString("student_given_name"));
                row.setCsiDate(rs.getString("csi_date"));
                row.setCaseManagerUserId(rs.getInt("user_id"));
                row.setCaseManagerSurname(rs.getString("case_manager_surname"));
                row.setCaseManagerGivenName(rs.getString("case_manager_given_name"));
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
