package com.timsanalytics.hcw.main.dao;

import com.timsanalytics.hcw.common.beans.KeyValue;
import com.timsanalytics.hcw.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.hcw.main.beans.Student;
import com.timsanalytics.hcw.main.beans.TierType;
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
                Student row = new Student();
                row.setStudentGuid(rs.getString("PERSON_GUID"));
                row.setStudentSurname(rs.getString("PERSON_SURNAME"));
                row.setStudentGivenName(rs.getString("PERSON_GIVEN_NAME"));
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

    private String getStudentList_SSP_RootQuery(ServerSidePaginationRequest<Student> serverSidePaginationRequest) {
        //noinspection StringBufferReplaceableByString
        StringBuilder query = new StringBuilder();
        query.append("              SELECT");
        query.append("                  PERSON.PERSON_GUID,");
        query.append("                  PERSON.PERSON_SURNAME,");
        query.append("                  PERSON.PERSON_GIVEN_NAME,");
        query.append("                  PERSON.STATUS,");
        query.append("                  PERSON.CREATED_ON,");
        query.append("                  PERSON.CREATED_BY,");
        query.append("                  PERSON.UPDATED_ON,");
        query.append("                  PERSON.UPDATED_BY");
        query.append("              FROM");
        query.append("                  HCW_DATA.PERSON");
        query.append("                  LEFT JOIN HCW_DATA.PERSON_TYPE ON PERSON_TYPE.PERSON_TYPE_GUID = PERSON.PERSON_TYPE_GUID");
        query.append("              WHERE");
        query.append("              (");
        query.append("                  PERSON.STATUS = 'Active'");
        query.append("                  AND PERSON_TYPE.PERSON_TYPE_NAME = 'Student'");
        query.append("                  AND\n");
        query.append(getStudentList_SSP_AdditionalWhereClause(serverSidePaginationRequest));
        query.append("              )");
        return query.toString();
    }

    private String getStudentList_SSP_AdditionalWhereClause(ServerSidePaginationRequest<Student> serverSidePaginationRequest) {
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

    public Student getStudentDetail(String studentGuid) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      PERSON_GUID,\n");
        query.append("      PERSON_SURNAME,\n");
        query.append("      PERSON_GIVEN_NAME,\n");
        query.append("      PERSON_GENDER,\n");
        query.append("      PERSON_DATE_OF_BIRTH,\n");
        query.append("      PERSON_SCHOOL,\n");
        query.append("      PERSON_GRADE,\n");
        query.append("      PERSON_ADDRESS_CURRENT,\n");
        query.append("      PERSON_ADDRESS_PREVIOUS,\n");
        query.append("      PERSON_PHONE_CURRENT,\n");
        query.append("      PERSON_PHONE_PREVIOUS,\n");
        query.append("      TIER_TYPE.TIER_TYPE_GUID,\n");
        query.append("      TIER_TYPE.TIER_TYPE_NAME\n");
        query.append("  FROM\n");
        query.append("      HCW_DATA.PERSON\n");
        query.append("      LEFT JOIN HCW_DATA.TIER_TYPE ON TIER_TYPE.TIER_TYPE_GUID = PERSON.TIER_TYPE_GUID\n");
        query.append("  WHERE\n");
        query.append("      PERSON_GUID = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{studentGuid}, (rs, rowNum) -> {
                Student row = new Student();
                row.setStudentGuid(rs.getString("PERSON_GUID"));
                row.setStudentSurname(rs.getString("PERSON_SURNAME"));
                row.setStudentGivenName(rs.getString("PERSON_GIVEN_NAME"));
                row.setStudentGender(rs.getString("PERSON_GENDER"));
                row.setStudentDateOfBirth(rs.getString("PERSON_DATE_OF_BIRTH"));
                row.setStudentSchool(rs.getString("PERSON_SCHOOL"));
                row.setStudentGrade(rs.getString("PERSON_GRADE"));
                row.setStudentAddressCurrent(rs.getString("PERSON_ADDRESS_CURRENT"));
                row.setStudentAddressPrevious(rs.getString("PERSON_ADDRESS_PREVIOUS"));
                row.setStudentPhoneCurrent(rs.getString("PERSON_PHONE_CURRENT"));
                row.setStudentPhonePrevious(rs.getString("PERSON_PHONE_PREVIOUS"));
                row.setTierTypeGuid(rs.getString("TIER_TYPE_GUID"));
                row.setTierTypeName(rs.getString("TIER_TYPE_NAME"));
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

    public Student updateStudent(Student student) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      HCW_DATA.PERSON\n");
        query.append("  SET\n");
        query.append("      PERSON.PERSON_SURNAME = ?,\n");
        query.append("      PERSON.PERSON_GIVEN_NAME = ?,\n");
        query.append("      PERSON.PERSON_GENDER = ?,\n");
        query.append("      PERSON.PERSON_DATE_OF_BIRTH = ?,\n");
        query.append("      PERSON.PERSON_SCHOOL = ?,\n");
        query.append("      PERSON.PERSON_GRADE = ?,\n");
        query.append("      PERSON.PERSON_ADDRESS_CURRENT = ?,\n");
        query.append("      PERSON.PERSON_ADDRESS_PREVIOUS = ?,\n");
        query.append("      PERSON.PERSON_PHONE_CURRENT = ?,\n");
        query.append("      PERSON.PERSON_PHONE_PREVIOUS = ?,\n");
        query.append("      PERSON.TIER_TYPE_GUID = ?\n");
        query.append("  WHERE\n");
        query.append("      PERSON.PERSON_GUID = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, student.getStudentSurname());
                        ps.setString(2, student.getStudentGivenName());
                        ps.setString(3, student.getStudentGender());
                        ps.setString(4, student.getStudentDateOfBirth());
                        ps.setString(5, student.getStudentSchool());
                        ps.setString(6, student.getStudentGrade());
                        ps.setString(7, student.getStudentAddressCurrent());
                        ps.setString(8, student.getStudentAddressPrevious());
                        ps.setString(9, student.getStudentPhoneCurrent());
                        ps.setString(10, student.getStudentPhonePrevious());
                        ps.setString(11, student.getTierTypeGuid());
                        ps.setString(12, student.getStudentGuid());
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
        query.append("      HCW_DATA.PERSON\n");
        query.append("  SET\n");
        query.append("      STATUS = 'Deleted'\n");
        query.append("  WHERE\n");
        query.append("      PERSON_GUID = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        this.logger.trace("PERSON_GUID=" + studentGuid);
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
