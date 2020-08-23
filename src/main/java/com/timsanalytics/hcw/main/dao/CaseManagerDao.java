package com.timsanalytics.hcw.main.dao;

import com.timsanalytics.hcw.common.beans.KeyValue;
import com.timsanalytics.hcw.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.hcw.main.beans.CaseManager;
import com.timsanalytics.hcw.main.dao.RowMappers.CaseManagerRowMapper;
import com.timsanalytics.hcw.utils.GenerateUuidService;
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
    private final GenerateUuidService generateUuidService;

    @Autowired
    public CaseManagerDao(JdbcTemplate mySqlAuthJdbcTemplate, GenerateUuidService generateUuidService) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.generateUuidService = generateUuidService;
    }

    public CaseManager createCaseManager(CaseManager caseManager) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      HCW_DATA.PERSON\n");
        query.append("      (\n");
        query.append("          PERSON.PERSON_GUID,\n");
        query.append("          PERSON.PERSON_SURNAME,\n");
        query.append("          PERSON.PERSON_GIVEN_NAME,\n");
        query.append("          PERSON.PERSON_GENDER,\n");
        query.append("          PERSON.STATUS\n");
        query.append("      )\n");
        query.append("      VALUES\n");
        query.append("      (\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          'Active'\n");
        query.append("      )\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        caseManager.setCaseManagerGuid(this.generateUuidService.GenerateUuid());
                        this.logger.trace("New CaseManager GUID: " + caseManager.getCaseManagerGuid());
                        ps.setString(1, caseManager.getCaseManagerGuid());
                        ps.setString(2, caseManager.getCaseManagerSurname());
                        ps.setString(3, caseManager.getCaseManagerGivenName());
                        ps.setString(4, caseManager.getCaseManagerGender());
                        return ps;
                    }
            );
            return this.getCaseManagerDetail(caseManager.getCaseManagerGuid());
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

        String sortColumn = serverSidePaginationRequest.getSortColumn();
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
        query.append("              PERSON_SURNAME,\n");
        query.append("              PERSON_GIVEN_NAME\n");
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
                CaseManager item = new CaseManager();
                item.setCaseManagerGuid(rs.getString("PERSON_GUID"));
                item.setCaseManagerSurname(rs.getString("PERSON_SURNAME"));
                item.setCaseManagerGivenName(rs.getString("PERSON_GIVEN_NAME"));
                return item;
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
        StringBuilder rootQuery = new StringBuilder();

        rootQuery.append("              SELECT");
        rootQuery.append("                  PERSON.PERSON_GUID,");
        rootQuery.append("                  PERSON.PERSON_SURNAME,");
        rootQuery.append("                  PERSON.PERSON_GIVEN_NAME,");
        rootQuery.append("                  PERSON.STATUS,");
        rootQuery.append("                  PERSON.CREATED_ON,");
        rootQuery.append("                  PERSON.CREATED_BY,");
        rootQuery.append("                  PERSON.UPDATED_ON,");
        rootQuery.append("                  PERSON.UPDATED_BY");
        rootQuery.append("              FROM");
        rootQuery.append("                  HCW_DATA.PERSON");
        rootQuery.append("                  LEFT JOIN HCW_DATA.PERSON_TYPE ON PERSON_TYPE.PERSON_TYPE_GUID = PERSON.PERSON_TYPE_GUID");
        rootQuery.append("              WHERE");
        rootQuery.append("              (");
        rootQuery.append("                  PERSON.STATUS = 'Active'");
        rootQuery.append("                  AND PERSON_TYPE.PERSON_TYPE_NAME = 'Case Manager'");
        rootQuery.append("                  AND\n");
        rootQuery.append(getCaseManagerList_SSP_AdditionalWhereClause(serverSidePaginationRequest));
        rootQuery.append("              )");
        return rootQuery.toString();
    }

    private String getCaseManagerList_SSP_AdditionalWhereClause(ServerSidePaginationRequest<CaseManager> serverSidePaginationRequest) {
        StringBuilder whereClause = new StringBuilder();
        String nameFilter = serverSidePaginationRequest.getNameFilter() != null ? serverSidePaginationRequest.getNameFilter() : "";

        // NAME FILTER CLAUSE
        if (!"".equalsIgnoreCase(nameFilter)) {
            whereClause.append("                  (\n");
            whereClause.append("                    UPPER(PERSON.PERSON_SURNAME) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                    OR");
            whereClause.append("                    UPPER(PERSON.PERSON_GIVEN_NAME) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                  )\n");
        } else {
            whereClause.append("                  (1=1)");
        }

        return whereClause.toString();
    }

    public CaseManager getCaseManagerDetail(String caseManagerGuid) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      PERSON_GUID,\n");
        query.append("      PERSON_SURNAME,\n");
        query.append("      PERSON_GIVEN_NAME,\n");
        query.append("      PERSON_GENDER,\n");
        query.append("      STATUS\n");
        query.append("  FROM\n");
        query.append("      HCW_DATA.PERSON\n");
        query.append("  WHERE\n");
        query.append("      PERSON_GUID = ?\n");
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
        query.append("      HCW_DATA.PERSON\n");
        query.append("  SET\n");
        query.append("      PERSON.PERSON_SURNAME = ?,\n");
        query.append("      PERSON.PERSON_GIVEN_NAME = ?,\n");
        query.append("      PERSON.PERSON_GENDER = ?\n");
        query.append("  WHERE\n");
        query.append("      PERSON.PERSON_GUID = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, caseManager.getCaseManagerSurname());
                        ps.setString(2, caseManager.getCaseManagerGivenName());
                        ps.setString(3, caseManager.getCaseManagerGender());
                        ps.setString(4, caseManager.getCaseManagerGuid());
                        return ps;
                    }
            );
            return this.getCaseManagerDetail(caseManager.getCaseManagerGuid());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public KeyValue deleteCaseManager(String caseManagerGuid) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      HCW_DATA.PERSON\n");
        query.append("  SET\n");
        query.append("      STATUS = 'Deleted'\n");
        query.append("  WHERE\n");
        query.append("      PERSON_GUID = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        this.logger.trace("PERSON_GUID=" + caseManagerGuid);
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, caseManagerGuid);
                        return ps;
                    }
            );
            return new KeyValue("caseManagerGuid", caseManagerGuid);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }
}
