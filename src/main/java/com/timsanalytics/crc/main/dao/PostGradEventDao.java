package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.main.beans.PostGradEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class PostGradEventDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;
    private final UtilsDao utilsDao;

    @Autowired
    public PostGradEventDao(JdbcTemplate mySqlAuthJdbcTemplate, UtilsDao utilsDao) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.utilsDao = utilsDao;
    }

    // BASIC CRUD

    public PostGradEvent createPostGradEvent(PostGradEvent postGradEvent) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.Student_Post_Grad_Event\n");
        query.append("      (\n");
        query.append("          student_id,\n");
        query.append("          post_grad_event_type_id,\n");
        query.append("          post_grad_event_date,\n");
        query.append("          deleted\n");
        query.append("      )\n");
        query.append("      VALUES\n");
        query.append("      (\n");
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
                        ps.setInt(1, postGradEvent.getStudentId());
                        ps.setInt(2, postGradEvent.getPostGradEventTypeId());
                        ps.setString(3, postGradEvent.getPostGradEventDate());
                        return ps;
                    }
            );
            int lastInsertId = this.utilsDao.getLastInsertId();
            this.logger.debug("New PostGradEvent ID: " + lastInsertId);
            return this.getPostGradEventDetail(lastInsertId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public List<PostGradEvent> getPostGradEventList() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      post_grad_event_id,\n");
        query.append("      student_id,\n");
        query.append("      post_grad_event_type_id,\n");
        query.append("      post_grad_event_date\n");
        query.append("  FROM\n");
        query.append("      CRC.Student_Post_Grad_Event\n");
        query.append("  WHERE\n");
        query.append("      deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      post_grad_event_date DESC\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                PostGradEvent row = new PostGradEvent();
                row.setPostGradEventId(rs.getInt("post_grad_event_id"));
                row.setStudentId(rs.getInt("student_id"));
                row.setPostGradEventTypeId(rs.getInt("post_grad_event_type_id"));
                row.setPostGradEventDate(rs.getString("post_grad_event_date"));
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

    public int getPostGradEventList_SSP_TotalRecords(ServerSidePaginationRequest<PostGradEvent> serverSidePaginationRequest) {
        return 0;
//        StringBuilder query = new StringBuilder();
//        query.append("          SELECT\n");
//        query.append("              COUNT(*)\n");
//        query.append("          FROM\n");
//        query.append("          -- ROOT QUERY\n");
//        query.append("          (\n");
//        query.append(getPostGradEventList_SSP_RootQuery(serverSidePaginationRequest));
//        query.append("          ) AS ROOT_QUERY\n");
//        query.append("          -- END ROOT QUERY\n");
//        try {
//            Integer count = this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{}, Integer.class);
//            return count == null ? 0 : count;
//        } catch (EmptyResultDataAccessException e) {
//            this.logger.error("EmptyResultDataAccessException: " + e);
//            return 0;
//        } catch (Exception e) {
//            this.logger.error("Exception: " + e);
//            return 0;
//        }
    }

    public List<PostGradEvent> getPostGradEventList_SSP(ServerSidePaginationRequest<PostGradEvent> serverSidePaginationRequest) {
        return null;
//        int pageStart = (serverSidePaginationRequest.getPageIndex()) * serverSidePaginationRequest.getPageSize();
//        int pageSize = serverSidePaginationRequest.getPageSize();
//
//        String defaultSortField = "postGradEvent_date";
//        String defaultSortDirection = "DESC";
//        String sortColumn = serverSidePaginationRequest.getSortColumn() != null ? serverSidePaginationRequest.getSortColumn() : defaultSortField;
//        String sortDirection = serverSidePaginationRequest.getSortDirection()  != null ? serverSidePaginationRequest.getSortDirection() : defaultSortDirection;
//
//        StringBuilder query = new StringBuilder();
//        query.append("  -- PAGINATION QUERY\n");
//        query.append("  SELECT\n");
//        query.append("      FILTER_SORT_QUERY.*\n");
//        query.append("  FROM\n");
//
//        query.append("      -- FILTER/SORT QUERY\n");
//        query.append("      (\n");
//        query.append("          SELECT\n");
//        query.append("              *\n");
//        query.append("          FROM\n");
//
//        query.append("          -- ROOT QUERY\n");
//        query.append("          (\n");
//        query.append(getPostGradEventList_SSP_RootQuery(serverSidePaginationRequest));
//        query.append("          ) AS ROOT_QUERY\n");
//        query.append("          -- END ROOT QUERY\n");
//
//        query.append("          ORDER BY\n");
//        query.append(sortColumn).append(" ").append(sortDirection.toUpperCase()).append(",\n");
//        query.append("              postGradEvent_date DESC,\n");
//        query.append("              surname,\n");
//        query.append("              given_name\n");
//        query.append("      ) AS FILTER_SORT_QUERY\n");
//        query.append("      -- END FILTER/SORT QUERY\n");
//
//        query.append("  LIMIT ?, ?\n");
//        query.append("  -- END PAGINATION QUERY\n");
//
//        this.logger.trace("SQL:\n" + query.toString());
//        this.logger.trace("pageStart=" + pageStart + ", pageSize=" + pageSize);
//
//        try {
//            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{
//                    pageStart,
//                    pageSize
//            }, (rs, rowNum) -> {
//                PostGradEvent row = new PostGradEvent();
//                row.setPostGradEventId(rs.getInt("student_postGradEvent_id"));
//                row.setPostGradEventDate(rs.getString("postGradEvent_date"));
//                row.setPostGradEventTypeName(rs.getString("postGradEvent_type_name"));
//                row.setInteractionTypeName(rs.getString("interaction_type_name"));
//                row.setStudentId(rs.getInt("student_id"));
//                row.setCaseManagerId(rs.getInt("case_manager_id"));
//                row.setStudentSurname(rs.getString("surname"));
//                row.setStudentGivenName(rs.getString("given_name"));
//                return row;
//            });
//        } catch (EmptyResultDataAccessException e) {
//            this.logger.error("EmptyResultDataAccessException: " + e);
//            return null;
//        } catch (Exception e) {
//            this.logger.error("Exception: " + e);
//            this.logger.error("pageStart=" + pageSize + ", pageSize=" + pageSize);
//            return null;
//        }
    }

    private String getPostGradEventList_SSP_RootQuery(ServerSidePaginationRequest<PostGradEvent> serverSidePaginationRequest) {
        //noinspection StringBufferReplaceableByString
        StringBuilder query = new StringBuilder();
        query.append("              SELECT\n");
        query.append("                  student_postGradEvent_id,\n");
        query.append("                  postGradEvent_date,\n");
        query.append("                  Ref_PostGradEvent_Type.name AS postGradEvent_type_name,\n");
        query.append("                  Ref_Interaction_Type.name AS interaction_type_name,\n");
        query.append("                  Person_Student.student_id,\n");
        query.append("                  Person_Student.surname,\n");
        query.append("                  Person_Student.given_name,\n");
        query.append("                  case_manager_id\n");
        query.append("              FROM\n");
        query.append("                  CRC.Student_PostGradEvent\n");
        query.append("                  LEFT JOIN CRC.Person_Student ON Person_Student.student_id = Student_PostGradEvent.student_id AND Person_Student.deleted = 0\n");
        query.append("                  LEFT JOIN CRC.Ref_PostGradEvent_Type ON Ref_PostGradEvent_Type.postGradEvent_type_id = Student_PostGradEvent.postGradEvent_type_id AND Ref_PostGradEvent_Type.deleted = 0\n");
        query.append("                  LEFT JOIN CRC.Ref_Interaction_Type ON Ref_Interaction_Type.interaction_type_id = Student_PostGradEvent.interaction_type_id AND Ref_Interaction_Type.deleted = 0\n");
        query.append("              WHERE\n");
        query.append("              (\n");
        query.append("                  Student_Post_Grad_Event.deleted = 0\n");
        query.append("                  AND\n");
        query.append("                  ");
        query.append(getPostGradEventList_SSP_AdditionalWhereClause(serverSidePaginationRequest));
        query.append("              )");
        return query.toString();
    }

    private String getPostGradEventList_SSP_AdditionalWhereClause(ServerSidePaginationRequest<PostGradEvent> serverSidePaginationRequest) {
        StringBuilder whereClause = new StringBuilder();
        String nameFilter = serverSidePaginationRequest.getNameFilter() != null ? serverSidePaginationRequest.getNameFilter() : "";

        // NAME FILTER CLAUSE
        if (!"".equalsIgnoreCase(nameFilter)) {
            whereClause.append("                  (\n");
            whereClause.append("                    UPPER(Student_PostGradEvent.surname) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                    OR");
            whereClause.append("                    UPPER(Student_PostGradEvent.given_name) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                  )\n");
        } else {
            whereClause.append("                  (1=1)");
        }

        return whereClause.toString();
    }

    public PostGradEvent getPostGradEventDetail(int postGradEventId) {
        return null;
//        StringBuilder query = new StringBuilder();
//        query.append("  SELECT\n");
//        query.append("      student_postGradEvent_id,\n");
//        query.append("      postGradEvent_date,\n");
//        query.append("      caregiver_comments,\n");
//        query.append("      case_manager_comments,\n");
//        query.append("      Ref_PostGradEvent_Type.postGradEvent_type_id AS postGradEvent_type_id,\n");
//        query.append("      Ref_PostGradEvent_Type.name AS postGradEvent_type_name,\n");
//        query.append("      Ref_Interaction_Type.interaction_type_id AS interaction_type_id,\n");
//        query.append("      Ref_Interaction_Type.name AS interaction_type_name,\n");
//        query.append("      Person_Student.student_id,\n");
//        query.append("      Person_Student.surname,\n");
//        query.append("      Person_Student.given_name,\n");
//        query.append("      Student_PostGradEvent.case_manager_id,\n");
//        query.append("      Person_Case_Manager.surname AS case_manager_surname,\n");
//        query.append("      Person_Case_Manager.given_name AS case_manager_given_name\n");
//        query.append("  FROM\n");
//        query.append("      CRC.Student_Post_Grad_Event\n");
//        query.append("      LEFT JOIN CRC.Person_Student ON Person_Student.student_id = Student_PostGradEvent.student_id AND Person_Student.deleted = 0\n");
//        query.append("      LEFT JOIN CRC.Ref_PostGradEvent_Type ON Ref_PostGradEvent_Type.postGradEvent_type_id = Student_PostGradEvent.postGradEvent_type_id AND Ref_PostGradEvent_Type.deleted = 0\n");
//        query.append("      LEFT JOIN CRC.Ref_Interaction_Type ON Ref_Interaction_Type.interaction_type_id = Student_PostGradEvent.interaction_type_id AND Ref_Interaction_Type.deleted = 0\n");
//        query.append("      LEFT JOIN CRC.Person_Case_Manager ON Person_Case_Manager.case_manager_id = Student_PostGradEvent.case_manager_id AND Person_Case_Manager.deleted = 0\n");
//        query.append("  WHERE\n");
//        query.append("      student_postGradEvent_id = ?\n");
//        this.logger.trace("SQL:\n" + query.toString());
//        try {
//            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{postGradEventId}, (rs, rowNum) -> {
//                PostGradEvent row = new PostGradEvent();
//                row.setPostGradEventId(rs.getInt("student_postGradEvent_id"));
//                row.setPostGradEventDate(rs.getString("postGradEvent_date"));
//                row.setCaregiverComments(rs.getString("caregiver_comments"));
//                row.setCaseManagerComments(rs.getString("case_manager_comments"));
//                row.setPostGradEventTypeId(rs.getInt("postGradEvent_type_id"));
//                row.setPostGradEventTypeName(rs.getString("postGradEvent_type_name"));
//                row.setInteractionTypeId(rs.getInt("interaction_type_id"));
//                row.setInteractionTypeName(rs.getString("interaction_type_name"));
//                row.setStudentId(rs.getInt("student_id"));
//                row.setStudentSurname(rs.getString("surname"));
//                row.setStudentGivenName(rs.getString("given_name"));
//                row.setCaseManagerId(rs.getInt("case_manager_id"));
//                row.setCaseManagerSurname(rs.getString("case_manager_surname"));
//                row.setCaseManagerGivenName(rs.getString("case_manager_given_name"));
//                row.setStudentGivenName(rs.getString("given_name"));
//                return row;
//            });
//        } catch (EmptyResultDataAccessException e) {
//            this.logger.error("EmptyResultDataAccessException: " + e);
//            return null;
//        } catch (Exception e) {
//            this.logger.error("Exception: " + e);
//            return null;
//        }
    }

    public PostGradEvent updatePostGradEvent(PostGradEvent postGradEvent) {
        return null;
//        StringBuilder query = new StringBuilder();
//        query.append("  UPDATE\n");
//        query.append("      CRC.Student_Post_Grad_Event\n");
//        query.append("  SET\n");
//        query.append("      case_manager_id = ?,\n");
//        query.append("      postGradEvent_date = ?,\n");
//        query.append("      postGradEvent_type_id = ?,\n");
//        query.append("      interaction_type_id = ?,\n");
//        query.append("      caregiver_comments = ?,\n");
//        query.append("      case_manager_comments = ?\n");
//        query.append("  WHERE\n");
//        query.append("      student_postGradEvent_id = ?\n");
//        this.logger.trace("SQL:\n" + query.toString());
//        try {
//            this.mySqlAuthJdbcTemplate.update(
//                    connection -> {
//                        PreparedStatement ps = connection.prepareStatement(query.toString());
//                        ps.setInt(1, postGradEvent.getCaseManagerId());
//                        ps.setString(2, postGradEvent.getPostGradEventDate());
//                        ps.setInt(3, postGradEvent.getPostGradEventTypeId());
//                        ps.setInt(4, postGradEvent.getInteractionTypeId());
//                        ps.setString(5, postGradEvent.getCaregiverComments());
//                        ps.setString(6, postGradEvent.getCaseManagerComments());
//                        ps.setInt(7, postGradEvent.getPostGradEventId());
//                        return ps;
//                    }
//            );
//            return this.getPostGradEventDetail(postGradEvent.getPostGradEventId());
//        } catch (EmptyResultDataAccessException e) {
//            this.logger.error("EmptyResultDataAccessException: " + e);
//            return null;
//        } catch (Exception e) {
//            this.logger.error("Exception: " + e);
//            return null;
//        }
    }

    public KeyValue deletePostGradEvent(String postGradEventId) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Student_Post_Grad_Event\n");
        query.append("  SET\n");
        query.append("      deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        this.logger.trace("postGradEventId=" + postGradEventId);
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, postGradEventId);
                        return ps;
                    }
            );
            return new KeyValue("postGradEventId", postGradEventId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    // JOINED TABLES

    public List<PostGradEvent> getPostGradEventListByStudentId(Integer studentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      post_grad_event_id,\n");
        query.append("      student_id,\n");
        query.append("      Student_Post_Grad_Event.post_grad_event_type_id,\n");
        query.append("      Ref_Post_Grad_Event_Type.post_grad_event_type_name,\n");
        query.append("      post_grad_event_date\n");
        query.append("  FROM\n");
        query.append("      CRC.Student_Post_Grad_Event\n");
        query.append("      LEFT JOIN CRC.Ref_Post_Grad_Event_Type ON Ref_Post_Grad_Event_Type.post_grad_event_type_id = Student_Post_Grad_Event.post_grad_event_type_id AND Ref_Post_Grad_Event_Type.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      student_id = ?\n");
        query.append("      AND Student_Post_Grad_Event.deleted = 0\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{studentId}, (rs, rowNum) -> {
                PostGradEvent row = new PostGradEvent();
                row.setPostGradEventId(rs.getInt("post_grad_event_id"));
                row.setStudentId(rs.getInt("student_id"));
                row.setPostGradEventTypeId(rs.getInt("post_grad_event_type_id"));
                row.setPostGradEventTypeName(rs.getString("post_grad_event_type_name"));
                row.setPostGradEventDate(rs.getString("post_grad_event_date"));
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
