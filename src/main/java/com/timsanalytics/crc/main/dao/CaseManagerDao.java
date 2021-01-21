package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.main.beans.CaseManager;
import com.timsanalytics.crc.main.dao.RowMappers.CaseManagerRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class CaseManagerDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;
    private final UtilsDao utilsDao;

    @Autowired
    public CaseManagerDao(JdbcTemplate mySqlAuthJdbcTemplate, UtilsDao utilsDao) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.utilsDao = utilsDao;
    }

    // BASIC CRUD

    public CaseManager createCaseManager(CaseManager caseManager) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.Person_Case_Manager\n");
        query.append("      (\n");
        query.append("          surname,\n");
        query.append("          given_name,\n");
        query.append("          phone,\n");
        query.append("          address,\n");
        query.append("          deleted\n");
        query.append("      )\n");
        query.append("      VALUES\n");
        query.append("      (\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          0\n");
        query.append("      )\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, caseManager.getCaseManagerSurname());
                        ps.setString(2, caseManager.getCaseManagerGivenName());
                        ps.setString(3, caseManager.getCaseManagerPhone());
                        ps.setString(4, caseManager.getCaseManagerEmail());
                        return ps;
                    }
            );
            int lastInsertId = this.utilsDao.getLastInsertId();
            this.logger.debug("New Case Manager ID: " + lastInsertId);
            return this.getCaseManagerDetail(lastInsertId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public List<CaseManager> getCaseManagerList() {
        StringBuilder query = new StringBuilder();
//        query.append("  SELECT\n");
//        query.append("      case_manager_id,\n");
//        query.append("      surname,\n");
//        query.append("      given_name\n");
//        query.append("  FROM\n");
//        query.append("      CRC.Person_Case_Manager\n");
//        query.append("  WHERE\n");
//        query.append("      deleted = 0\n");
//        query.append("  ORDER BY\n");
//        query.append("      surname,\n");
//        query.append("      given_name\n");
        query.append("  SELECT\n");
        query.append("      Auth_User.user_id AS case_manager_id,\n");
        query.append("      Auth_User.surname,\n");
        query.append("      Auth_User.given_name,\n");
        query.append("      Auth_User_Role.role_id\n");
        query.append("  FROM\n");
        query.append("      CRC.Auth_User\n");
        query.append("      LEFT JOIN CRC.Auth_User_Role ON Auth_User_Role.user_id = Auth_User.user_id AND role_id = 5\n");
        query.append("  WHERE\n");
        query.append("      Auth_User.deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      Auth_User.surname,\n");
        query.append("      Auth_User.given_name\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                CaseManager row = new CaseManager();
                row.setCaseManagerId(rs.getInt("case_manager_id"));
                row.setCaseManagerSurname(rs.getString("surname"));
                row.setCaseManagerGivenName(rs.getString("given_name"));
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
    
    public int getCaseManagerList_SSP_TotalRecords(ServerSidePaginationRequest<CaseManager> serverSidePaginationRequest) {
        StringBuilder query = new StringBuilder();
        query.append("          SELECT\n");
        query.append("              COUNT(*)\n");
        query.append("          FROM\n");
        query.append("          -- ROOT QUERY\n");
        query.append("          (\n");
        query.append(getCaseManagerList_SSP_RootQuery(serverSidePaginationRequest));
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

    public List<CaseManager> getCaseManagerList_SSP(ServerSidePaginationRequest<CaseManager> serverSidePaginationRequest) {
        int pageStart = (serverSidePaginationRequest.getPageIndex()) * serverSidePaginationRequest.getPageSize();
        int pageSize = serverSidePaginationRequest.getPageSize();

        String sortColumn = serverSidePaginationRequest.getSortColumn() != null ? serverSidePaginationRequest.getSortColumn() : "surname";
        String sortDirection = serverSidePaginationRequest.getSortDirection();

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
        query.append(getCaseManagerList_SSP_RootQuery(serverSidePaginationRequest));
        query.append("          ) AS ROOT_QUERY\n");
        query.append("          -- END ROOT QUERY\n");

        query.append("          ORDER BY\n");
        query.append(sortColumn).append(" ").append(sortDirection.toUpperCase()).append(",\n");
        query.append("              surname,\n");
        query.append("              given_name\n");
        query.append("      ) AS FILTER_SORT_QUERY\n");
        query.append("      -- END FILTER/SORT QUERY\n");

        query.append("  LIMIT ?, ?\n");
        query.append("  -- END PAGINATION QUERY\n");

        this.logger.trace("SQL:\n" + query.toString());
        this.logger.trace("pageStart=" + pageStart + ", pageSize=" + pageSize);

        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{
                    pageStart,
                    pageSize
            }, (rs, rowNum) -> {
                CaseManager row = new CaseManager();
                row.setCaseManagerId(rs.getInt("case_manager_id"));
                row.setCaseManagerSurname(rs.getString("surname"));
                row.setCaseManagerGivenName(rs.getString("given_name"));
                row.setCaseManagerPhone(rs.getString("phone"));
                row.setCaseManagerEmail(rs.getString("email"));
                row.setStudentCount(rs.getInt("student_count"));
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

    private String getCaseManagerList_SSP_RootQuery(ServerSidePaginationRequest<CaseManager> serverSidePaginationRequest) {
        //noinspection StringBufferReplaceableByString
        StringBuilder query = new StringBuilder();
        query.append("              SELECT\n");
        query.append("                  Auth_User.user_id,\n");
        query.append("                  Auth_User.username,\n");
        query.append("                  Auth_User.surname,\n");
        query.append("                  Auth_User.given_name\n");
        query.append("              FROM\n");
        query.append("                  CRC.Auth_User\n");
        query.append("                  LEFT JOIN CRC.Auth_User_Role ON Auth_User_Role.user_id = Auth_User.user_id AND Auth_User_Role.deleted = 0\n");
        query.append("              WHERE\n");
        query.append("                  Auth_User.deleted = 0\n");
        query.append("                  AND Auth_User_Role.role_id = 5\n");
        query.append("                  AND\n");
        query.append("                  ");
        query.append(getCaseManagerList_SSP_AdditionalWhereClause(serverSidePaginationRequest));
        query.append("              )");
        return query.toString();
    }

    private String getCaseManagerList_SSP_AdditionalWhereClause(ServerSidePaginationRequest<CaseManager> serverSidePaginationRequest) {
        StringBuilder whereClause = new StringBuilder();
        String nameFilter = serverSidePaginationRequest.getNameFilter() != null ? serverSidePaginationRequest.getNameFilter() : "";

        // NAME FILTER CLAUSE
        if (!"".equalsIgnoreCase(nameFilter)) {
            whereClause.append("                  (\n");
            whereClause.append("                    UPPER(Person_Case_Manager.surname) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                    OR");
            whereClause.append("                    UPPER(Person_Case_Manager.given_name) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                  )\n");
        } else {
            whereClause.append("                  (1=1)");
        }

        return whereClause.toString();
    }

    public CaseManager getCaseManagerDetail(int caseManagerGuid) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      case_manager_id,\n");
        query.append("      surname,\n");
        query.append("      given_name,\n");
        query.append("      phone,\n");
        query.append("      email\n");
        query.append("  FROM\n");
        query.append("      CRC.Person_Case_Manager\n");
        query.append("  WHERE\n");
        query.append("      case_manager_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{caseManagerGuid}, new CaseManagerRowMapper());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public CaseManager updateCaseManager(CaseManager caseManager) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Person_Case_Manager\n");
        query.append("  SET\n");
        query.append("      surname = ?,\n");
        query.append("      given_name = ?,\n");
        query.append("      phone = ?,\n");
        query.append("      email = ?\n");
        query.append("  WHERE\n");
        query.append("      case_manager_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, caseManager.getCaseManagerSurname());
                        ps.setString(2, caseManager.getCaseManagerGivenName());
                        ps.setString(3, caseManager.getCaseManagerPhone());
                        ps.setString(4, caseManager.getCaseManagerEmail());
                        ps.setInt(5, caseManager.getCaseManagerId());
                        return ps;
                    }
            );
            return this.getCaseManagerDetail(caseManager.getCaseManagerId());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public KeyValue deleteCaseManager(String caseManagerId) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Person_Case_Manager\n");
        query.append("  SET\n");
        query.append("      deleted = 1\n");
        query.append("  WHERE\n");
        query.append("      case_manager_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        this.logger.trace("id=" + caseManagerId);
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, caseManagerId);
                        return ps;
                    }
            );
            return new KeyValue("caseManagerId", caseManagerId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    // JOINED QUERIES

    public CaseManager getCaseManagerDetailByStudentId(int studentId) {
        StringBuilder query = new StringBuilder();
//        query.append("  SELECT\n");
//        query.append("      Person_Case_Manager.case_manager_id,\n");
//        query.append("      Person_Case_Manager.surname,\n");
//        query.append("      Person_Case_Manager.given_name,\n");
//        query.append("      Rel_Student_Case_Manager.student_case_manager_id,\n");
//        query.append("      Rel_Student_Case_Manager.start_date\n");
//        query.append("  FROM\n");
//        query.append("      CRC.Rel_Student_Case_Manager\n");
//        query.append("      LEFT JOIN CRC.Person_Case_Manager ON Person_Case_Manager.case_manager_id = Rel_Student_Case_Manager.case_manager_id AND Person_Case_Manager.deleted = 0\n");
//        query.append("  WHERE\n");
//        query.append("      student_id = ?\n");
//        query.append("      AND Rel_Student_Case_Manager.deleted = 0\n");
//        query.append("  ORDER BY\n");
//        query.append("      start_date DESC\n");
//        query.append("  LIMIT 0, 1\n");
        query.append("  SELECT\n");
        query.append("      Auth_User.user_id AS case_manager_id,\n");
        query.append("      Auth_User.surname,\n");
        query.append("      Auth_User.given_name,\n");
        query.append("      Rel_Student_Case_Manager.student_case_manager_id,\n");
        query.append("      Rel_Student_Case_Manager.start_date\n");
        query.append("  FROM\n");
        query.append("      CRC.Rel_Student_Case_Manager\n");
        query.append("      LEFT JOIN CRC.Auth_User ON Auth_User.user_id = Rel_Student_Case_Manager.case_manager_id AND Auth_User.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      student_id = ?\n");
        query.append("      AND Auth_User.deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      start_date DESC\n");
        query.append("      LIMIT 0, 1\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{studentId}, (rs, rowNum) -> {
                CaseManager row = new CaseManager();
                row.setCaseManagerId(rs.getInt("case_manager_id"));
                row.setCaseManagerSurname(rs.getString("surname"));
                row.setCaseManagerGivenName(rs.getString("given_name"));
                row.setRelationshipId(rs.getInt("student_case_manager_id"));
                row.setRelationshipStartDate(rs.getString("start_date"));
                return row;
            });
        } catch (EmptyResultDataAccessException e) {
            return new CaseManager();
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }
}
