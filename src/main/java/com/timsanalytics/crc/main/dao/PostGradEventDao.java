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
        query.append("          post_grad_event_date,\n");
        query.append("          post_grad_event_type_id,\n");
        query.append("          post_grad_event_comments,\n");
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
                        ps.setInt(1, postGradEvent.getStudentId());
                        ps.setString(2, postGradEvent.getPostGradEventDate());
                        ps.setInt(3, postGradEvent.getPostGradEventTypeId());
                        ps.setString(4, postGradEvent.getPostGradEventComments());
                        return ps;
                    }
            );
            int lastInsertId = this.utilsDao.getLastInsertId();
            this.logger.trace("New Post-Grad Event ID: " + lastInsertId);
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
        StringBuilder query = new StringBuilder();
        query.append("          SELECT\n");
        query.append("              COUNT(*)\n");
        query.append("          FROM\n");
        query.append("          -- ROOT QUERY\n");
        query.append("          (\n");
        query.append(getPostGradEventList_SSP_RootQuery(serverSidePaginationRequest));
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

    public List<PostGradEvent> getPostGradEventList_SSP(ServerSidePaginationRequest<PostGradEvent> serverSidePaginationRequest) {
        int pageStart = (serverSidePaginationRequest.getPageIndex()) * serverSidePaginationRequest.getPageSize();
        int pageSize = serverSidePaginationRequest.getPageSize();

        String defaultSortField = "post_grad_event_date";
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
        query.append(getPostGradEventList_SSP_RootQuery(serverSidePaginationRequest));
        query.append("          ) AS ROOT_QUERY\n");
        query.append("          -- END ROOT QUERY\n");

        query.append("          ORDER BY\n");
        query.append(sortColumn).append(" ").append(sortDirection.toUpperCase()).append(",\n");
        query.append("              post_grad_event_date DESC,\n");
        query.append("              student_surname,\n");
        query.append("              student_given_name\n");
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
                PostGradEvent row = new PostGradEvent();
                row.setPostGradEventId(rs.getInt("post_grad_event_id"));
                row.setPostGradEventDate(rs.getString("post_grad_event_date"));
                row.setPostGradEventTypeName(rs.getString("post_grad_event_type_name"));
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("student_surname"));
                row.setStudentGivenName(rs.getString("student_given_name"));
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

    private String getPostGradEventList_SSP_RootQuery(ServerSidePaginationRequest<PostGradEvent> serverSidePaginationRequest) {
        //noinspection StringBufferReplaceableByString
        StringBuilder query = new StringBuilder();
        query.append("              SELECT\n");
        query.append("                  post_grad_event_id,\n");
        query.append("                  Student_Post_Grad_Event.student_id,\n");
        query.append("                  Person_Student.surname AS student_surname,\n");
        query.append("                  Person_Student.given_name AS student_given_name,\n");
        query.append("                  Student_Post_Grad_Event.post_grad_event_type_id,\n");
        query.append("                  Ref_Post_Grad_Event_Type.post_grad_event_type_name,\n");
        query.append("                  post_grad_event_date\n");
        query.append("              FROM\n");
        query.append("                  CRC.Student_Post_Grad_Event\n");
        query.append("                  LEFT JOIN CRC.Person_Student ON Person_Student.student_id = Student_Post_Grad_Event.student_id\n");
        query.append("                  LEFT JOIN CRC.Ref_Post_Grad_Event_Type ON Ref_Post_Grad_Event_Type.post_grad_event_type_id = Student_Post_Grad_Event.post_grad_event_id\n");
        query.append("              WHERE\n");
        query.append("              (\n");
        query.append("                  Student_Post_Grad_Event.deleted = 0\n");
        query.append("                  AND\n");
        query.append(getPostGradEventList_SSP_AdditionalWhereClause(serverSidePaginationRequest));
        query.append("              )\n");
        return query.toString();
    }

    private String getPostGradEventList_SSP_AdditionalWhereClause(ServerSidePaginationRequest<PostGradEvent> serverSidePaginationRequest) {
        StringBuilder whereClause = new StringBuilder();
        String nameFilter = serverSidePaginationRequest.getNameFilter() != null ? serverSidePaginationRequest.getNameFilter() : "";

        // NAME FILTER CLAUSE
        if (!"".equalsIgnoreCase(nameFilter)) {
            whereClause.append("                  (\n");
            whereClause.append("                    UPPER(Student_PostGradEvent.surname) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                    OR\n");
            whereClause.append("                    UPPER(Student_PostGradEvent.given_name) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                  )\n");
        } else {
            whereClause.append("                  (1=1)\n");
        }

        return whereClause.toString();
    }

    public PostGradEvent getPostGradEventDetail(int postGradEventId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      post_grad_event_id,\n");
        query.append("      Student_Post_Grad_Event.student_id,\n");
        query.append("      Person_Student.surname AS student_surname,\n");
        query.append("      Person_Student.given_name AS student_given_name,\n");
        query.append("      Student_Post_Grad_Event.post_grad_event_type_id,\n");
        query.append("      Ref_Post_Grad_Event_Type.post_grad_event_type_name,\n");
        query.append("      post_grad_event_date,\n");
        query.append("      post_grad_event_comments\n");
        query.append("  FROM\n");
        query.append("      CRC.Student_Post_Grad_Event\n");
        query.append("      LEFT JOIN CRC.Person_Student ON Person_Student.student_id = Student_Post_Grad_Event.student_id\n");
        query.append("      LEFT JOIN CRC.Ref_Post_Grad_Event_Type ON Ref_Post_Grad_Event_Type.post_grad_event_type_id = Student_Post_Grad_Event.post_grad_event_type_id\n");
        query.append("  WHERE\n");
        query.append("      post_grad_event_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{postGradEventId}, (rs, rowNum) -> {
                PostGradEvent row = new PostGradEvent();
                row.setPostGradEventId(rs.getInt("post_grad_event_id"));
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("student_surname"));
                row.setStudentGivenName(rs.getString("student_given_name"));
                row.setPostGradEventTypeId(rs.getInt("post_grad_event_type_id"));
                row.setPostGradEventTypeName(rs.getString("post_grad_event_type_name"));
                row.setPostGradEventDate(rs.getString("post_grad_event_date"));
                row.setPostGradEventComments(rs.getString("post_grad_event_comments"));
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

    public PostGradEvent updatePostGradEvent(PostGradEvent postGradEvent) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Student_Post_Grad_Event\n");
        query.append("  SET\n");
        query.append("      student_id = ?,\n");
        query.append("      post_grad_event_type_id = ?,\n");
        query.append("      post_grad_event_date = ?,\n");
        query.append("      post_grad_event_comments = ?\n");
        query.append("  WHERE\n");
        query.append("      post_grad_event_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, postGradEvent.getStudentId());
                        ps.setInt(2, postGradEvent.getPostGradEventTypeId());
                        ps.setString(3, postGradEvent.getPostGradEventDate());
                        ps.setString(4, postGradEvent.getPostGradEventComments());
                        ps.setInt(5, postGradEvent.getPostGradEventId());
                        return ps;
                    }
            );
            return this.getPostGradEventDetail(postGradEvent.getPostGradEventId());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public KeyValue deletePostGradEvent(String postGradEventId) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Student_Post_Grad_Event\n");
        query.append("  SET\n");
        query.append("      deleted = 1\n");
        query.append("  WHERE\n");
        query.append("      post_grad_event_id = ?\n");
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
        query.append("  ORDER BY\n");
        query.append("      post_grad_event_date DESC\n");
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
