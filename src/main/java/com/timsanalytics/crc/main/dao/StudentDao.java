package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.main.beans.Relationship;
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
        query.append("      CRC.Person_Student\n");
        query.append("      (\n");
        query.append("          surname,\n");
        query.append("          given_name,\n");
        query.append("          sex,\n");
        query.append("          dob,\n");
        query.append("          school,\n");
        query.append("          school_level_type_id,\n");
        query.append("          class_level_type_id,\n");
        query.append("          class_repeat_year_type_id,\n");
        query.append("          impairment_type_id,\n");
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
                        ps.setString(5, student.getStudentSchool());
                        ps.setInt(6, student.getSchoolLevelTypeId());
                        ps.setInt(7, student.getClassLevelTypeId());
                        ps.setInt(8, student.getClassRepeatYearTypeId());
                        ps.setInt(9, student.getImpairmentTypeId());
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

    public List<Student> getStudentList() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      student_id,\n");
        query.append("      surname,\n");
        query.append("      given_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Person_Student\n");
        query.append("  WHERE\n");
        query.append("      deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      surname,\n");
        query.append("      given_name\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                Student row = new Student();
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("surname"));
                row.setStudentGivenName(rs.getString("given_name"));
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

    public List<Student> getStudentList_SSP(ServerSidePaginationRequest<Student> serverSidePaginationRequest) {
        int pageStart = (serverSidePaginationRequest.getPageIndex()) * serverSidePaginationRequest.getPageSize();
        int pageSize = serverSidePaginationRequest.getPageSize();

        String defaultSortField = "surname";
        String sortColumn = serverSidePaginationRequest.getSortColumn() != null ? serverSidePaginationRequest.getSortColumn() : defaultSortField;
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
        query.append("              "); // Spacing for output
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
                Student row = new Student();
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("surname"));
                row.setStudentGivenName(rs.getString("given_name"));
                row.setCaregiverSurname(rs.getString("caregiver_surname"));
                row.setCaregiverGivenName(rs.getString("caregiver_given_name"));
                row.setCaregiverAddress(rs.getString("caregiver_address"));
                row.setCaregiverPhone(rs.getString("caregiver_phone"));
                row.setRelationshipTierTypeName(rs.getString("tier_type_name"));
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
        // REF: 'greatest-n-per-group'
        // REF: https://intellipaat.com/community/5064/sql-join-selecting-the-last-records-in-a-one-to-many-relationship
        //noinspection StringBufferReplaceableByString
        StringBuilder query = new StringBuilder();
        query.append("              SELECT\n");
        query.append("                  Person_Student.student_id,\n");
        query.append("                  Person_Student.surname,\n");
        query.append("                  Person_Student.given_name,\n");
        query.append("                  Person_Student.sex,\n");
        query.append("                  Person_Student.dob,\n");
        query.append("                  Person_Student.school,\n");
        query.append("                  Person_Student.grade,\n");
        query.append("                  Person_Caregiver.caregiver_id,\n");
        query.append("                  Person_Caregiver.surname AS caregiver_surname,\n");
        query.append("                  Person_Caregiver.given_name AS caregiver_given_name,\n");
        query.append("                  Person_Caregiver.address AS caregiver_address,\n");
        query.append("                  Person_Caregiver.phone AS caregiver_phone,\n");
        query.append("                  Rel_Student_Caregiver.start_date,\n");
        query.append("                  Ref_Tier_Type.tier_type_id,\n");
        query.append("                  Ref_Tier_Type.name AS tier_type_name\n");
        query.append("              FROM\n");
        query.append("                  CRC.Person_Student\n");
        query.append("                  LEFT JOIN CRC.Rel_Student_Caregiver ON Rel_Student_Caregiver.student_id = Person_Student.student_id AND Rel_Student_Caregiver.deleted = 0\n");
        query.append("                  LEFT OUTER JOIN CRC.Rel_Student_Caregiver effectiveDateComparison ON\n");
        query.append("                  (\n");
        query.append("                      effectiveDateComparison.student_id = Person_Student.student_id\n");
        query.append("                      AND\n");
        query.append("                      (\n");
        query.append("                          Rel_Student_Caregiver.start_date < effectiveDateComparison.start_date\n");
        query.append("                          OR\n");
        query.append("                          (\n");
        query.append("                              Rel_Student_Caregiver.start_date = effectiveDateComparison.start_date\n");
        query.append("                              AND\n");
        query.append("                              Rel_Student_Caregiver.student_id > effectiveDateComparison.student_id\n");
        query.append("                          )\n");
        query.append("                      )\n");
        query.append("                  )\n");
        query.append("                  LEFT JOIN CRC.Person_Caregiver ON Person_Caregiver.caregiver_id = Rel_Student_Caregiver.caregiver_id AND Person_Caregiver.deleted = 0\n");
        query.append("                  LEFT JOIN CRC.Ref_Tier_Type on Ref_Tier_Type.tier_type_id = Rel_Student_Caregiver.tier_type_id AND Ref_Tier_Type.deleted = 0\n");
        query.append("              WHERE\n");
        query.append("              (\n");
        query.append("                  Person_Student.deleted = 0\n");
        query.append("                  AND effectiveDateComparison.student_id IS NULL\n");
        query.append("                  AND ");
        query.append(getStudentList_SSP_AdditionalWhereClause(serverSidePaginationRequest));
        query.append("              )\n");
        return query.toString();
    }

    private String getStudentList_SSP_AdditionalWhereClause(ServerSidePaginationRequest<Student> serverSidePaginationRequest) {
        StringBuilder whereClause = new StringBuilder();
        String nameFilter = serverSidePaginationRequest.getNameFilter() != null ? serverSidePaginationRequest.getNameFilter() : "";

        // NAME FILTER CLAUSE
        if (!"".equalsIgnoreCase(nameFilter)) {
            whereClause.append("                  (\n");
            whereClause.append("                    UPPER(Person_Student.surname) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                    OR\n");
            whereClause.append("                    UPPER(Person_Student.given_name) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                  )\n");
        } else {
            whereClause.append("                  (1=1)\n");
        }

        return whereClause.toString();
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

    public Student getStudentDetail(Integer studentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      student_id,\n");
        query.append("      surname,\n");
        query.append("      given_name,\n");
        query.append("      sex,\n");
        query.append("      dob,\n");
        query.append("      school,\n");
        query.append("      grade,\n");
        query.append("      school_level_type_id,\n");
        query.append("      school_level.name AS school_level_type_name,\n");
        query.append("      class_level_type_id,\n");
        query.append("      class_level.name AS class_level_type_name,\n");
        query.append("      class_repeat_year_type_id,\n");
        query.append("      Person_Student.impairment_type_id,\n");
        query.append("      Ref_Impairment_Type.impairment_type_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Person_Student\n");
        query.append("      LEFT JOIN CRC.Ref_School_Class_Type school_level ON school_level.school_class_type_id = Person_Student.school_level_type_id AND school_level.deleted = 0\n");
        query.append("      LEFT JOIN CRC.Ref_School_Class_Type class_level ON class_level.school_class_type_id = Person_Student.class_level_type_id AND class_level.deleted = 0\n");
        query.append("      LEFT JOIN CRC.Ref_Impairment_Type ON Ref_Impairment_Type.impairment_type_id = Person_Student.impairment_type_id AND Ref_Impairment_Type.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      student_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{studentId}, (rs, rowNum) -> {
                Student row = new Student();
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("surname"));
                row.setStudentGivenName(rs.getString("given_name"));
                row.setStudentGender(rs.getString("sex"));
                row.setStudentDateOfBirth(rs.getString("dob"));
                row.setStudentSchool(rs.getString("school"));
                row.setStudentGrade(rs.getString("grade"));
                row.setSchoolLevelTypeId(rs.getInt("school_level_type_id"));
                row.setSchoolLevelTypeName(rs.getString("school_level_type_name"));
                row.setClassLevelTypeId(rs.getInt("class_level_type_id"));
                row.setClassLevelTypeName(rs.getString("class_level_type_name"));
                row.setClassRepeatYearTypeId(rs.getInt("class_repeat_year_type_id"));
                row.setImpairmentTypeId(rs.getInt("impairment_type_id"));
                row.setImpairmentTypeName(rs.getString("impairment_type_name"));
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
        query.append("      CRC.Person_Student\n");
        query.append("  SET\n");
        query.append("      surname = ?,\n");
        query.append("      given_name = ?,\n");
        query.append("      sex = ?,\n");
        query.append("      dob = ?,\n");
        query.append("      school = ?,\n");
        query.append("      school_level_type_id = ?,\n");
        query.append("      class_level_type_id = ?,\n");
        query.append("      class_repeat_year_type_id = ?,\n");
        query.append("      impairment_type_id = ?\n");
        query.append("  WHERE\n");
        query.append("      student_id = ?\n");
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
                        ps.setInt(6, student.getSchoolLevelTypeId());
                        ps.setInt(7, student.getClassLevelTypeId());
                        ps.setInt(8, student.getClassRepeatYearTypeId());
                        ps.setInt(9, student.getImpairmentTypeId());
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
        query.append("      CRC.Person_Student\n");
        query.append("  SET\n");
        query.append("      deleted = 1\n");
        query.append("  WHERE\n");
        query.append("      student_id = ?\n");
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

    // FILTERED LISTS

    public List<Student> getStudentListBySponsorId(Integer sponsorId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Person_Student.student_id,\n");
        query.append("      Person_Student.surname,\n");
        query.append("      Person_Student.given_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Rel_Student_Sponsor\n");
        query.append("      LEFT JOIN CRC.Person_Student ON Person_Student.student_id = Rel_Student_Sponsor.student_id AND Person_Student.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      Rel_Student_Sponsor.sponsor_id = ?\n");
        query.append("      AND Rel_Student_Sponsor.deleted = 0\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{sponsorId}, (rs, rowNum) -> {
                Student row = new Student();
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("surname"));
                row.setStudentGivenName(rs.getString("given_name"));
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

    // OTHER QUERIES

    public List<Student> checkDuplicateStudentRecord(Student student) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      student_id,\n");
        query.append("      surname,\n");
        query.append("      given_name,\n");
        query.append("      sex,\n");
        query.append("      dob\n");
        query.append("  FROM\n");
        query.append("      CRC.Person_Student\n");
        query.append("  WHERE\n");
        query.append("      LOWER(given_name) = LOWER(?)\n");
        query.append("      AND LOWER(surname) = LOWER(?)\n");
        query.append("      AND deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      surname,\n");
        query.append("      given_name\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{
                    student.getStudentGivenName(),
                    student.getStudentSurname()
            }, (rs, rowNum) -> {
                Student row = new Student();
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("surname"));
                row.setStudentGivenName(rs.getString("given_name"));
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
