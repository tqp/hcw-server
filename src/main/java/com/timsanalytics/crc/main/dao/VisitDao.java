package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.main.beans.Visit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class VisitDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;
    private final UtilsDao utilsDao;

    @Autowired
    public VisitDao(JdbcTemplate mySqlAuthJdbcTemplate, UtilsDao utilsDao) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.utilsDao = utilsDao;
    }

    // BASIC CRUD

    public Visit createVisit(Visit visit) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.Student_Visit\n");
        query.append("      (\n");
        query.append("          student_id,\n");
        query.append("          case_manager_id,\n");
        query.append("          visit_date,\n");
        query.append("          visit_type_id,\n");
        query.append("          interaction_type_id,\n");
        query.append("          caregiver_comments,\n");
        query.append("          case_manager_comments,\n");
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
        query.append("          0\n");
        query.append("      )\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, visit.getStudentId());
                        ps.setInt(2, visit.getCaseManagerId());
                        ps.setString(3, visit.getVisitDate());
                        ps.setInt(4, visit.getVisitTypeId());
                        ps.setInt(5, visit.getInteractionTypeId());
                        ps.setString(6, visit.getCaregiverComments());
                        ps.setString(7, visit.getCaseManagerComments());
                        return ps;
                    }
            );
            int lastInsertId = this.utilsDao.getLastInsertId();
            this.logger.debug("New Visit ID: " + lastInsertId);
            return this.getVisitDetail(lastInsertId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public List<Visit> getVisitList() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      student_visit_id,\n");
        query.append("      student_id,\n");
        query.append("      case_manager_id\n");
        query.append("  FROM\n");
        query.append("      CRC.Student_Visit\n");
        query.append("  WHERE\n");
        query.append("      deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      student_visit_id\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                Visit row = new Visit();
                row.setVisitId(rs.getInt("student_visit_id"));
                row.setStudentId(rs.getInt("student_id"));
                row.setCaseManagerId(rs.getInt("case_manager_id"));
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

    public int getVisitList_SSP_TotalRecords(ServerSidePaginationRequest<Visit> serverSidePaginationRequest) {
        StringBuilder query = new StringBuilder();
        query.append("          SELECT\n");
        query.append("              COUNT(*)\n");
        query.append("          FROM\n");
        query.append("          -- ROOT QUERY\n");
        query.append("          (\n");
        query.append(getVisitList_SSP_RootQuery(serverSidePaginationRequest));
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

    public List<Visit> getVisitList_SSP(ServerSidePaginationRequest<Visit> serverSidePaginationRequest) {
        int pageStart = (serverSidePaginationRequest.getPageIndex()) * serverSidePaginationRequest.getPageSize();
        int pageSize = serverSidePaginationRequest.getPageSize();

        String defaultSortField = "visit_date";
        String defaultSortDirection = "DESC";
        String sortColumn = serverSidePaginationRequest.getSortColumn() != null ? serverSidePaginationRequest.getSortColumn() : defaultSortField;
        String sortDirection = serverSidePaginationRequest.getSortDirection()  != null ? serverSidePaginationRequest.getSortDirection() : defaultSortDirection;

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
        query.append(getVisitList_SSP_RootQuery(serverSidePaginationRequest));
        query.append("          ) AS ROOT_QUERY\n");
        query.append("          -- END ROOT QUERY\n");

        query.append("          ORDER BY\n");
        query.append(sortColumn).append(" ").append(sortDirection.toUpperCase()).append(",\n");
        query.append("              visit_date DESC,\n");
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
                Visit row = new Visit();
                row.setVisitId(rs.getInt("student_visit_id"));
                row.setVisitDate(rs.getString("visit_date"));
                row.setVisitTypeName(rs.getString("visit_type_name"));
                row.setInteractionTypeName(rs.getString("interaction_type_name"));
                row.setStudentId(rs.getInt("student_id"));
                row.setCaseManagerId(rs.getInt("case_manager_id"));
                row.setStudentSurname(rs.getString("surname"));
                row.setStudentGivenName(rs.getString("given_name"));
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

    private String getVisitList_SSP_RootQuery(ServerSidePaginationRequest<Visit> serverSidePaginationRequest) {
        //noinspection StringBufferReplaceableByString
        StringBuilder query = new StringBuilder();
        query.append("              SELECT\n");
        query.append("                  student_visit_id,\n");
        query.append("                  visit_date,\n");
        query.append("                  Ref_Visit_Type.name AS visit_type_name,\n");
        query.append("                  Ref_Interaction_Type.name AS interaction_type_name,\n");
        query.append("                  Person_Student.student_id,\n");
        query.append("                  Person_Student.surname,\n");
        query.append("                  Person_Student.given_name,\n");
        query.append("                  case_manager_id\n");
        query.append("              FROM\n");
        query.append("                  CRC.Student_Visit\n");
        query.append("                  LEFT JOIN CRC.Person_Student ON Person_Student.student_id = Student_Visit.student_id AND Person_Student.deleted = 0\n");
        query.append("                  LEFT JOIN CRC.Ref_Visit_Type ON Ref_Visit_Type.visit_type_id = Student_Visit.visit_type_id AND Ref_Visit_Type.deleted = 0\n");
        query.append("                  LEFT JOIN CRC.Ref_Interaction_Type ON Ref_Interaction_Type.interaction_type_id = Student_Visit.interaction_type_id AND Ref_Interaction_Type.deleted = 0\n");
        query.append("              WHERE\n");
        query.append("              (\n");
        query.append("                  Student_Visit.deleted = 0\n");
        query.append("                  AND\n");
        query.append("                  ");
        query.append(getVisitList_SSP_AdditionalWhereClause(serverSidePaginationRequest));
        query.append("              )");
        return query.toString();
    }

    private String getVisitList_SSP_AdditionalWhereClause(ServerSidePaginationRequest<Visit> serverSidePaginationRequest) {
        StringBuilder whereClause = new StringBuilder();
        String nameFilter = serverSidePaginationRequest.getNameFilter() != null ? serverSidePaginationRequest.getNameFilter() : "";

        // NAME FILTER CLAUSE
        if (!"".equalsIgnoreCase(nameFilter)) {
            whereClause.append("                  (\n");
            whereClause.append("                    UPPER(Student_Visit.surname) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                    OR");
            whereClause.append("                    UPPER(Student_Visit.given_name) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                  )\n");
        } else {
            whereClause.append("                  (1=1)");
        }

        return whereClause.toString();
    }

    public Visit getVisitDetail(int visitId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      student_visit_id,\n");
        query.append("      visit_date,\n");
        query.append("      caregiver_comments,\n");
        query.append("      case_manager_comments,\n");
        query.append("      Ref_Visit_Type.visit_type_id AS visit_type_id,\n");
        query.append("      Ref_Visit_Type.name AS visit_type_name,\n");
        query.append("      Ref_Interaction_Type.interaction_type_id AS interaction_type_id,\n");
        query.append("      Ref_Interaction_Type.name AS interaction_type_name,\n");
        query.append("      Person_Student.student_id,\n");
        query.append("      Person_Student.surname,\n");
        query.append("      Person_Student.given_name,\n");
        query.append("      Student_Visit.case_manager_id,\n");
        query.append("      Person_Case_Manager.surname AS case_manager_surname,\n");
        query.append("      Person_Case_Manager.given_name AS case_manager_given_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Student_Visit\n");
        query.append("      LEFT JOIN CRC.Person_Student ON Person_Student.student_id = Student_Visit.student_id AND Person_Student.deleted = 0\n");
        query.append("      LEFT JOIN CRC.Ref_Visit_Type ON Ref_Visit_Type.visit_type_id = Student_Visit.visit_type_id AND Ref_Visit_Type.deleted = 0\n");
        query.append("      LEFT JOIN CRC.Ref_Interaction_Type ON Ref_Interaction_Type.interaction_type_id = Student_Visit.interaction_type_id AND Ref_Interaction_Type.deleted = 0\n");
        query.append("      LEFT JOIN CRC.Person_Case_Manager ON Person_Case_Manager.case_manager_id = Student_Visit.case_manager_id AND Person_Case_Manager.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      student_visit_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{visitId}, (rs, rowNum) -> {
                Visit row = new Visit();
                row.setVisitId(rs.getInt("student_visit_id"));
                row.setVisitDate(rs.getString("visit_date"));
                row.setCaregiverComments(rs.getString("caregiver_comments"));
                row.setCaseManagerComments(rs.getString("case_manager_comments"));
                row.setVisitTypeId(rs.getInt("visit_type_id"));
                row.setVisitTypeName(rs.getString("visit_type_name"));
                row.setInteractionTypeId(rs.getInt("interaction_type_id"));
                row.setInteractionTypeName(rs.getString("interaction_type_name"));
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("surname"));
                row.setStudentGivenName(rs.getString("given_name"));
                row.setCaseManagerId(rs.getInt("case_manager_id"));
                row.setCaseManagerSurname(rs.getString("case_manager_surname"));
                row.setCaseManagerGivenName(rs.getString("case_manager_given_name"));
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

    public Visit updateVisit(Visit visit) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Student_Visit\n");
        query.append("  SET\n");
        query.append("      case_manager_id = ?,\n");
        query.append("      visit_date = ?,\n");
        query.append("      visit_type_id = ?,\n");
        query.append("      interaction_type_id = ?,\n");
        query.append("      caregiver_comments = ?,\n");
        query.append("      case_manager_comments = ?\n");
        query.append("  WHERE\n");
        query.append("      student_visit_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, visit.getCaseManagerId());
                        ps.setString(2, visit.getVisitDate());
                        ps.setInt(3, visit.getVisitTypeId());
                        ps.setInt(4, visit.getInteractionTypeId());
                        ps.setString(5, visit.getCaregiverComments());
                        ps.setString(6, visit.getCaseManagerComments());
                        ps.setInt(7, visit.getVisitId());
                        return ps;
                    }
            );
            return this.getVisitDetail(visit.getVisitId());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public KeyValue deleteVisit(String visitId) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Student_Visit\n");
        query.append("  SET\n");
        query.append("      deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        this.logger.trace("visitId=" + visitId);
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, visitId);
                        return ps;
                    }
            );
            return new KeyValue("visitId", visitId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    // JOINED TABLES

    public List<Visit> getVisitListByStudentId(Integer studentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      student_visit_id,\n");
        query.append("      student_id,\n");
        query.append("      Student_Visit.case_manager_id,\n");
        query.append("      Person_Case_Manager.surname AS case_manager_surname,\n");
        query.append("      Person_Case_Manager.given_name AS case_manager_given_name,\n");
        query.append("      visit_date,\n");
        query.append("      Student_Visit.visit_type_id,\n");
        query.append("      Ref_Visit_Type.name AS visit_type_name,\n");
        query.append("      Student_Visit.interaction_type_id,\n");
        query.append("      Ref_Interaction_Type.name AS interaction_type_name,\n");
        query.append("      caregiver_comments,\n");
        query.append("      case_manager_comments\n");
        query.append("  FROM\n");
        query.append("      CRC.Student_Visit\n");
        query.append("      LEFT JOIN CRC.Ref_Visit_Type ON Ref_Visit_Type.visit_type_id = Student_Visit.visit_type_id AND Ref_Visit_Type.deleted = 0\n");
        query.append("      LEFT JOIN CRC.Ref_Interaction_Type ON Ref_Interaction_Type.interaction_type_id = Student_Visit.interaction_type_id AND Ref_Interaction_Type.deleted = 0\n");
        query.append("      LEFT JOIN CRC.Person_Case_Manager ON Person_Case_Manager.case_manager_id = Student_Visit.case_manager_id AND Person_Case_Manager.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      student_id = ?\n");
        query.append("      AND Student_Visit.deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      visit_date DESC\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{studentId}, (rs, rowNum) -> {
                Visit row = new Visit();
                row.setVisitId(rs.getInt("student_visit_id"));
                row.setStudentId(rs.getInt("student_id"));
                row.setCaseManagerId(rs.getInt("case_manager_id"));
                row.setCaseManagerSurname(rs.getString("case_manager_surname"));
                row.setCaseManagerGivenName(rs.getString("case_manager_given_name"));
                row.setVisitDate(rs.getString("visit_date"));
                row.setVisitTypeId(rs.getInt("visit_type_id"));
                row.setVisitTypeName(rs.getString("visit_type_name"));
                row.setInteractionTypeId(rs.getInt("interaction_type_id"));
                row.setInteractionTypeName(rs.getString("interaction_type_name"));
                row.setCaregiverComments(rs.getString("caregiver_comments"));
                row.setCaseManagerComments(rs.getString("case_manager_comments"));
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
