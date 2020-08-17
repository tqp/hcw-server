package com.timsanalytics.hcw.main.dao;

import com.timsanalytics.hcw.common.beans.KeyValue;
import com.timsanalytics.hcw.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.hcw.main.beans.Student;
import com.timsanalytics.hcw.main.dao.RowMappers.StudentRowMapper;
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
public class StudentDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;
    private final GenerateUuidService generateUuidService;

    @Autowired
    public StudentDao(JdbcTemplate mySqlAuthJdbcTemplate, GenerateUuidService generateUuidService) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.generateUuidService = generateUuidService;
    }

    public Student createStudent(Student student) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      HCW_DATA.STUDENT\n");
        query.append("      (\n");
        query.append("          STUDENT.STUDENT_GUID,\n");
        query.append("          STUDENT.STUDENT_SURNAME,\n");
        query.append("          STUDENT.STUDENT_GIVEN_NAME,\n");
        query.append("          STUDENT.STUDENT_GENDER,\n");
        query.append("          STUDENT.STATUS\n");
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
                        student.setStudentGuid(this.generateUuidService.GenerateUuid());
                        this.logger.trace("New Student GUID: " + student.getStudentGuid());
                        ps.setString(1, student.getStudentGuid());
                        ps.setString(2, student.getStudentSurname());
                        ps.setString(3, student.getStudentGivenName());
                        ps.setString(4, student.getStudentGender());
                        return ps;
                    }
            );
            return this.getStudentDetail(student.getStudentGuid());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }
    
    public int getStudentList_SSP_TotalRecords(ServerSidePaginationRequest<Student> serverSidePaginationRequest) {
        StringBuilder query = new StringBuilder();
        query.append("          SELECT\n");
        query.append("              COUNT(*)\n");
        query.append("          FROM\n");
        query.append("          -- ROOT QUERY\n");
        query.append("          (\n");
        query.append(getStudentList_SSP_RootQuery(serverSidePaginationRequest));
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

    public List<Student> getStudentList_SSP(ServerSidePaginationRequest<Student> serverSidePaginationRequest) {
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
        query.append(getStudentList_SSP_RootQuery(serverSidePaginationRequest));
        query.append("          ) AS ROOT_QUERY\n");
        query.append("          -- END ROOT QUERY\n");

        query.append("          ORDER BY\n");
        query.append(sortColumn).append(" ").append(sortDirection.toUpperCase()).append(",\n");
        query.append("              STUDENT_SURNAME,\n");
        query.append("              STUDENT_GIVEN_NAME\n");
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
                Student item = new Student();
                item.setStudentGuid(rs.getString("STUDENT_GUID"));
                item.setStudentSurname(rs.getString("STUDENT_SURNAME"));
                item.setStudentGivenName(rs.getString("STUDENT_GIVEN_NAME"));
                return item;
            });
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    private String getStudentList_SSP_RootQuery(ServerSidePaginationRequest<Student> serverSidePaginationRequest) {
        //noinspection StringBufferReplaceableByString
        StringBuilder rootQuery = new StringBuilder();

        rootQuery.append("              SELECT");
        rootQuery.append("                  STUDENT.STUDENT_GUID,");
        rootQuery.append("                  STUDENT.STUDENT_SURNAME,");
        rootQuery.append("                  STUDENT.STUDENT_GIVEN_NAME,");
        rootQuery.append("                  STUDENT.STATUS,");
        rootQuery.append("                  STUDENT.CREATED_ON,");
        rootQuery.append("                  STUDENT.CREATED_BY,");
        rootQuery.append("                  STUDENT.UPDATED_ON,");
        rootQuery.append("                  STUDENT.UPDATED_BY");
        rootQuery.append("              FROM");
        rootQuery.append("                  HCW_DATA.STUDENT");
        rootQuery.append("              WHERE");
        rootQuery.append("              (");
        rootQuery.append("                  STUDENT.STATUS = 'Active'");
        rootQuery.append("                  AND\n");
        rootQuery.append(getStudentList_SSP_AdditionalWhereClause(serverSidePaginationRequest));
        rootQuery.append("              )");
        return rootQuery.toString();
    }

    private String getStudentList_SSP_AdditionalWhereClause(ServerSidePaginationRequest<Student> serverSidePaginationRequest) {
        StringBuilder whereClause = new StringBuilder();
        String nameFilter = serverSidePaginationRequest.getNameFilter() != null ? serverSidePaginationRequest.getNameFilter() : "";

        // NAME FILTER CLAUSE
        if (!"".equalsIgnoreCase(nameFilter)) {
            whereClause.append("                  (\n");
            whereClause.append("                    UPPER(STUDENT.STUDENT_SURNAME) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                    OR");
            whereClause.append("                    UPPER(STUDENT.STUDENT_GIVEN_NAME) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                  )\n");
        } else {
            whereClause.append("                  (1=1)");
        }

        return whereClause.toString();
    }

    public Student getStudentDetail(String studentGuid) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      STUDENT_GUID,\n");
        query.append("      STUDENT_SURNAME,\n");
        query.append("      STUDENT_GIVEN_NAME,\n");
        query.append("      STUDENT_GENDER,\n");
        query.append("      STATUS\n");
        query.append("  FROM\n");
        query.append("      HCW_DATA.STUDENT\n");
        query.append("  WHERE\n");
        query.append("      STUDENT_GUID = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{studentGuid}, new StudentRowMapper());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public Student updateStudent(Student student) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      HCW_DATA.STUDENT\n");
        query.append("  SET\n");
        query.append("      STUDENT.STUDENT_SURNAME = ?,\n");
        query.append("      STUDENT.STUDENT_GIVEN_NAME = ?,\n");
        query.append("      STUDENT.STUDENT_GENDER = ?\n");
        query.append("  WHERE\n");
        query.append("      STUDENT.STUDENT_GUID = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, student.getStudentSurname());
                        ps.setString(2, student.getStudentGivenName());
                        ps.setString(3, student.getStudentGender());
                        ps.setString(4, student.getStudentGuid());
                        return ps;
                    }
            );
            return this.getStudentDetail(student.getStudentGuid());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public KeyValue deleteStudent(String studentGuid) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      HCW_DATA.STUDENT\n");
        query.append("  SET\n");
        query.append("      STATUS = 'Deleted'\n");
        query.append("  WHERE\n");
        query.append("      STUDENT_GUID = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        this.logger.trace("STUDENT_GUID=" + studentGuid);
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, studentGuid);
                        return ps;
                    }
            );
            return new KeyValue("studentGuid", studentGuid);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }
}
