package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.main.beans.Student;
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
    private final UtilsDao utilsDao;

    @Autowired
    public StudentDao(JdbcTemplate mySqlAuthJdbcTemplate, UtilsDao utilsDao) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.utilsDao = utilsDao;
    }

    public Student createStudent(Student student) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.Person\n");
        query.append("      (\n");
        query.append("          last_name,\n");
        query.append("          first_name,\n");
        query.append("          sex,\n");
        query.append("          dob,\n");
        query.append("          person_type_id,\n");
        query.append("          tier_type_id,\n");
        query.append("          deleted\n");
        query.append("      )\n");
        query.append("      VALUES\n");
        query.append("      (\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          1,\n");
        query.append("          0\n");
        query.append("      )\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, student.getStudentSurname());
                        ps.setString(2, student.getStudentGivenName());
                        ps.setString(3, student.getStudentGender());
                        ps.setString(4, student.getStudentDateOfBirth());
                        ps.setInt(5, student.getTierTypeId());
                        return ps;
                    }
            );
            int lastInsertId = this.utilsDao.getLastInsertId();
            this.logger.debug("New Student ID: " + lastInsertId);
            return this.getStudentDetail(lastInsertId);
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
        query.append("              last_name,\n");
        query.append("              first_name\n");
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
                row.setStudentId(rs.getInt("id"));
                row.setStudentSurname(rs.getString("last_name"));
                row.setStudentGivenName(rs.getString("first_name"));
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
        query.append("              SELECT\n");
        query.append("                  Person.id,\n");
        query.append("                  Person.first_name,\n");
        query.append("                  Person.last_name\n");
        query.append("              FROM\n");
        query.append("                  CRC.Person\n");
        query.append("              WHERE\n");
        query.append("              (\n");
        query.append("                      Person.Deleted = 0\n");
        query.append("                      AND Person.person_type_id = 1\n");
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
            whereClause.append("                    UPPER(Person.last_name) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                    OR");
            whereClause.append("                    UPPER(Person.first_name) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                  )\n");
        } else {
            whereClause.append("                  (1=1)");
        }

        return whereClause.toString();
    }

    public Student getStudentDetail(Integer studentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Person.id,\n");
        query.append("      last_name,\n");
        query.append("      first_name,\n");
        query.append("      sex,\n");
        query.append("      dob,\n");
        query.append("      school,\n");
        query.append("      grade,\n");
        query.append("      address,\n");
        query.append("      phone,\n");
        query.append("      tier_type_id,\n");
        query.append("      TierType.name tier_type_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Person\n");
        query.append("      LEFT JOIN CRC.TierType ON CRC.TierType.id = Person.tier_type_id\n");
        query.append("  WHERE\n");
        query.append("      Person.id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{studentId}, (rs, rowNum) -> {
                Student row = new Student();
                row.setStudentId(rs.getInt("id"));
                row.setStudentSurname(rs.getString("last_name"));
                row.setStudentGivenName(rs.getString("first_name"));
                row.setStudentGender(rs.getString("sex"));
                row.setStudentDateOfBirth(rs.getString("dob"));
                row.setStudentSchool(rs.getString("school"));
                row.setStudentGrade(rs.getString("grade"));
                row.setStudentAddress(rs.getString("address"));
                row.setStudentPhone(rs.getString("phone"));
                row.setTierTypeId(rs.getInt("tier_type_id"));
                row.setTierTypeName(rs.getString("tier_type_name"));
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
        query.append("      CRC.Person\n");
        query.append("  SET\n");
        query.append("      last_name = ?,\n");
        query.append("      first_name = ?,\n");
        query.append("      sex = ?,\n");
        query.append("      dob = ?,\n");
        query.append("      school = ?,\n");
        query.append("      grade = ?,\n");
        query.append("      address = ?,\n");
        query.append("      phone = ?,\n");
        query.append("      tier_type_id = ?\n");
        query.append("  WHERE\n");
        query.append("      id = ?\n");
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
                        ps.setString(7, student.getStudentAddress());
                        ps.setString(8, student.getStudentPhone());
                        ps.setInt(9, student.getTierTypeId());
                        ps.setInt(10, student.getStudentId());
                        return ps;
                    }
            );
            return this.getStudentDetail(student.getStudentId());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public KeyValue deleteStudent(String studentId) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Person\n");
        query.append("  SET\n");
        query.append("      deleted = 1'\n");
        query.append("  WHERE\n");
        query.append("      id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        this.logger.trace("id=" + studentId);
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, studentId);
                        return ps;
                    }
            );
            return new KeyValue("studentId", studentId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }
}
