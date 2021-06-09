package com.timsanalytics.crc.auth.authCommon.dao;

import com.timsanalytics.crc.auth.authCommon.beans.Role;
import com.timsanalytics.crc.auth.authCommon.beans.User;
import com.timsanalytics.crc.auth.authCommon.dao.rowMappers.RoleRowMapper;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.main.dao.UtilsDao;
import com.timsanalytics.crc.utils.BCryptEncoderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final BCryptEncoderService bCryptEncoderService;
    private final UtilsDao utilsDao;

    @Qualifier("mySqlAuthJdbcTemplate")
    private final JdbcTemplate mySqlAuthJdbcTemplate;

    public UserDao(BCryptEncoderService bCryptEncoderService,
                   JdbcTemplate mySqlAuthJdbcTemplate,
                   UtilsDao utilsDao) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.bCryptEncoderService = bCryptEncoderService;
        this.utilsDao = utilsDao;
    }

    public User createUser(User user, User loggedInUser) {
        this.logger.debug("UserDao -> createUser");
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.Auth_User\n");
        query.append("      (\n");
        query.append("          username,\n");
        query.append("          surname,\n");
        query.append("          given_name,\n");
        query.append("          position_id,\n");
        query.append("          password,\n");
        query.append("          password_set,\n");
        query.append("          created_on,\n");
        query.append("          created_by,\n");
        query.append("          updated_on,\n");
        query.append("          updated_by,\n");
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
        query.append("          NOW(),\n");
        query.append("          ?,\n");
        query.append("          NOW(),\n");
        query.append("          ?,\n");
        query.append("          0\n");
        query.append("      )\n");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, user.getUserUsername());
                        ps.setString(2, user.getUserSurname());
                        ps.setString(3, user.getUserGivenName());
                        ps.setInt(4, user.getPositionId());
                        ps.setString(5, user.getPassword() != null ? this.bCryptEncoderService.encode(user.getPassword()) : null);
                        ps.setString(6, user.getPassword() != null ? now.toString() : null);
                        ps.setInt(7, -1);
                        ps.setInt(8, -1);
                        return ps;
                    });
            int lastInsertId = this.utilsDao.getLastInsertId();
            this.logger.debug("New User ID: " + lastInsertId);
            return this.getUserDetail(lastInsertId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public User getUserDetail(Integer userId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      user_id,\n");
        query.append("      username,\n");
        query.append("      password,\n");
        query.append("      password_set,\n");
        query.append("      password_reset,\n");
        query.append("      last_login,\n");
        query.append("      login_count,\n");
        query.append("      surname,\n");
        query.append("      given_name,\n");
        query.append("      Auth_User.position_id,\n");
        query.append("      Auth_Position.position_name,\n");
        query.append("      user_profile_photo_url,\n");
        query.append("      Auth_User.created_on,\n");
        query.append("      Auth_User.created_by,\n");
        query.append("      Auth_User.updated_on,\n");
        query.append("      Auth_User.updated_by,\n");
        query.append("      Auth_User.deleted\n");
        query.append("  FROM\n");
        query.append("      CRC.Auth_User\n");
        query.append("      LEFT JOIN CRC.Auth_Position ON Auth_Position.position_id = Auth_User.position_id AND Auth_Position.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      user_id = ?\n");
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{userId}, (rs, rowNum) -> {
                User row = new User();
                row.setUserId(rs.getInt("user_id"));
                row.setUserUsername(rs.getString("username"));
                row.setPassword(rs.getString("password"));
                row.setPasswordSet(rs.getString("password_set"));
                row.setPasswordReset(rs.getInt("password_reset"));
                row.setLastLogin(rs.getString("last_login"));
                row.setLoginCount(rs.getInt("login_count"));
                row.setUserSurname(rs.getString("surname"));
                row.setUserGivenName(rs.getString("given_name"));
                row.setPositionId(rs.getInt("position_id"));
                row.setPositionName(rs.getString("position_name"));
                row.setPicture(rs.getString("user_profile_photo_url"));
                row.setCreatedOn(rs.getTimestamp("created_on"));
                row.setCreatedBy(rs.getString("created_by"));
                row.setUpdatedOn(rs.getTimestamp("updated_on"));
                row.setUpdatedBy(rs.getString("updated_by"));
                row.setStatus(rs.getInt("deleted"));
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

    public List<User> getUserList() {
        this.logger.debug("UserDao -> getUserList");
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Auth_User.user_id,\n");
        query.append("      username,\n");
        query.append("      given_name,\n");
        query.append("      surname,\n");
        query.append("      Auth_User.position_id,\n");
        query.append("      Auth_Position.position_name,\n");
        query.append("      last_login,\n");
        query.append("      login_count,\n");
        query.append("      user_profile_photo_url\n");
        query.append("  FROM\n");
        query.append("      CRC.Auth_User\n");
        query.append("      LEFT JOIN CRC.Auth_Position ON Auth_Position.position_id = Auth_User.position_id AND Auth_Position.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      Auth_User.deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      given_name,\n");
        query.append("      surname,\n");
        query.append("      Auth_User.given_name\n");
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                User row = new User();
                row.setUserId(rs.getInt("user_id"));
                row.setUserUsername(rs.getString("username"));
                row.setUserGivenName(rs.getString("given_name"));
                row.setUserSurname(rs.getString("surname"));
                row.setPositionId(rs.getInt("position_id"));
                row.setPositionName(rs.getString("position_name"));
                row.setLastLogin(rs.getString("last_login"));
                row.setLoginCount(rs.getInt("login_count"));
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

    public List<User> getUserList_SSP(ServerSidePaginationRequest<User> serverSidePaginationRequest) {
        int pageStart = (serverSidePaginationRequest.getPageIndex()) * serverSidePaginationRequest.getPageSize();
        int pageSize = serverSidePaginationRequest.getPageSize();

        String defaultSortField = "username";
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
        query.append(getUserList_SSP_RootQuery(serverSidePaginationRequest));
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
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{
                    pageStart,
                    pageSize
            }, (rs, rowNum) -> {
                User row = new User();
                row.setUserId(rs.getInt("user_id"));
                row.setUserUsername(rs.getString("username"));
                row.setUserSurname(rs.getString("surname"));
                row.setUserGivenName(rs.getString("given_name"));
                row.setLastLogin(rs.getString("last_login"));
                row.setLoginCount(rs.getInt("login_count"));
                row.setPositionId(rs.getInt("position_id"));
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

    private String getUserList_SSP_RootQuery(ServerSidePaginationRequest<User> serverSidePaginationRequest) {
        //noinspection StringBufferReplaceableByString
        StringBuilder query = new StringBuilder();
        query.append("              SELECT\n");
        query.append("                  Auth_User.user_id,\n");
        query.append("                  username,\n");
        query.append("                  surname,\n");
        query.append("                  given_name,\n");
        query.append("                  position_id,\n");
        query.append("                  last_login,\n");
        query.append("                  login_count\n");
        query.append("              FROM\n");
        query.append("                  CRC.Auth_User\n");
        query.append("                  LEFT JOIN CRC.Auth_User_Role ON Auth_User_Role.user_id = Auth_User.user_id AND Auth_User_Role.deleted = 0\n");
        query.append("              WHERE\n");
        query.append("              (\n");
        query.append("                  Auth_User.deleted = 0\n");
        query.append("                  AND ");
        query.append(getStudentList_SSP_AdditionalWhereClause(serverSidePaginationRequest));
        query.append("              )\n");
        query.append("              GROUP BY\n");
        query.append("                  username\n");
        return query.toString();
    }

    private String getStudentList_SSP_AdditionalWhereClause(ServerSidePaginationRequest<User> serverSidePaginationRequest) {
        StringBuilder whereClause = new StringBuilder();
        String nameFilter = serverSidePaginationRequest.getNameFilter() != null ? serverSidePaginationRequest.getNameFilter() : "";

        // NAME FILTER CLAUSE
        if (!"".equalsIgnoreCase(nameFilter)) {
            whereClause.append("                  (\n");
            whereClause.append("                    UPPER(Auth_User.surname) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                    OR\n");
            whereClause.append("                    UPPER(Auth_User.given_name) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                    OR\n");
            whereClause.append("                    UPPER(Auth_User.username) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                  )\n");
        } else {
            whereClause.append("                  (1=1)\n");
        }

        return whereClause.toString();
    }

    public int getUserList_SSP_TotalRecords(ServerSidePaginationRequest<User> serverSidePaginationRequest) {
        StringBuilder query = new StringBuilder();
        query.append("          SELECT\n");
        query.append("              COUNT(*)\n");
        query.append("          FROM\n");
        query.append("          -- ROOT QUERY\n");
        query.append("          (\n");
        query.append(getUserList_SSP_RootQuery(serverSidePaginationRequest));
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

    public User updateUser(User User, User loggedInUser) {
        this.logger.debug("UserDao -> updateUser");
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Auth_User\n");
        query.append("  SET\n");
        query.append("      username = ?,\n");
        query.append("      surname = ?,\n");
        query.append("      given_name = ?,\n");
        query.append("      position_id = ?,\n");
        query.append("      updated_on = NOW(),\n");
        query.append("      updated_by = ?\n");
        query.append("  WHERE\n");
        query.append("      user_id = ?\n");
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
//                        ps.setInt(1, loggedInUser.getUsername());
                        ps.setString(1, User.getUserUsername());
                        ps.setString(2, User.getUserSurname());
                        ps.setString(3, User.getUserGivenName());
                        ps.setInt(4, User.getPositionId());
                        ps.setInt(5, -1);
                        ps.setInt(6, User.getUserId());
                        return ps;
                    }
            );
            return this.getUserDetail(User.getUserId());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public User deleteUser(Integer userId) {
        this.logger.debug("UserDao -> deleteUser: " + userId);
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Auth_User\n");
        query.append("  SET\n");
        query.append("      deleted = 1\n");
        query.append("  WHERE\n");
        query.append("      user_id = ?\n");
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, userId);
                        return ps;
                    }
            );
            return this.getUserDetail(userId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public User purgeUser(Integer userId) {
        this.logger.debug("UserDao -> purgeUser: " + userId);
        StringBuilder query = new StringBuilder();
        query.append("  DELETE FROM\n");
        query.append("      CRC.App_User\n");
        query.append("  WHERE\n");
        query.append("      user_id = ?\n");
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, userId);
                        return ps;
                    }
            );
            return this.getUserDetail(userId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    // RELATED CRUD

    public User disableUser(Integer userId) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      USER\n");
        query.append("  SET\n");
        query.append("      USER.STATUS = 'Disabled'\n");
        query.append("  WHERE\n");
        query.append("      USER.USER_GUID = ?\n");
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, userId);
                        return ps;
                    }
            );
            return this.getUserDetail(userId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public User enableUser(Integer userId) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      USER\n");
        query.append("  SET\n");
        query.append("      USER.STATUS = 'Active'\n");
        query.append("  WHERE\n");
        query.append("      USER.USER_GUID = ?\n");
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, userId);
                        return ps;
                    }
            );
            return this.getUserDetail(userId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public User updateMyProfile(User User, User loggedInUser) {
        this.logger.debug("UserDao -> updateMyProfile: userGuid=" + loggedInUser.getUserId());
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      USER\n");
        query.append("  SET\n");
        query.append("      USER.UPDATED_ON = NOW(),\n");
        query.append("      USER.UPDATED_BY = ?\n");
        query.append("  WHERE\n");
        query.append("      USER.USER_GUID = ?\n");
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, loggedInUser.getUserId());
                        ps.setInt(2, User.getUserId());
                        return ps;
                    }
            );
            return this.getUserDetail(User.getUserId());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    // NON-CRUD DAOs

    public User updatePassword(User user, User loggedInUser) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Auth_User\n");
        query.append("  SET\n");
        query.append("      password = ?,\n");
        query.append("      password_set = NOW(),\n");
        query.append("      password_reset = 0,\n");
        query.append("      updated_on = NOW(),\n");
        query.append("      updated_by = ?\n");
        query.append("  WHERE\n");
        query.append("      user_id = ?\n");
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, this.bCryptEncoderService.encode(user.getPassword()));
                        ps.setInt(2, -1);
                        ps.setInt(3, user.getUserId());
                        return ps;
                    }
            );
            return this.getUserDetail(user.getUserId());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public User resetPassword(User user, User loggedInUser) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Auth_User\n");
        query.append("  SET\n");
        query.append("      password = ?,\n");
        query.append("      password_set = NULL,\n");
        query.append("      password_reset = 1,\n");
        query.append("      updated_on = NOW(),\n");
        query.append("      updated_by = ?\n");
        query.append("  WHERE\n");
        query.append("      user_id = ?\n");
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, this.bCryptEncoderService.encode(user.getPassword()));
                        ps.setInt(2, -1);
                        ps.setInt(3, user.getUserId());
                        return ps;
                    }
            );
            return this.getUserDetail(user.getUserId());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public User getUserDetailByUsername(String username) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      user_id,\n");
        query.append("      username,\n");
        query.append("      password,\n");
        query.append("      password_set,\n");
        query.append("      password_reset,\n");
        query.append("      last_login,\n");
        query.append("      login_count,\n");
        query.append("      surname,\n");
        query.append("      given_name,\n");
        query.append("      Auth_User.position_id,\n");
        query.append("      position_name,\n");
        query.append("      user_profile_photo_url,\n");
        query.append("      Auth_User.created_on,\n");
        query.append("      Auth_User.created_by,\n");
        query.append("      Auth_User.updated_on,\n");
        query.append("      Auth_User.updated_by,\n");
        query.append("      Auth_User.deleted\n");
        query.append("  FROM\n");
        query.append("      CRC.Auth_User\n");
        query.append("      LEFT JOIN CRC.Auth_Position ON Auth_Position.position_id = Auth_User.position_id AND Auth_Position.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      username = ?\n");
        query.append("      AND Auth_User.deleted = 0\n");
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{username}, (rs, rowNum) -> {
                User row = new User();
                row.setUserId(rs.getInt("user_id"));
                row.setUserUsername(rs.getString("username"));
                row.setPassword(rs.getString("password"));
                row.setPasswordSet(rs.getString("password_set"));
                row.setPasswordReset(rs.getInt("password_reset"));
                row.setLastLogin(rs.getString("last_login"));
                row.setLoginCount(rs.getInt("login_count"));
                row.setUserSurname(rs.getString("surname"));
                row.setUserGivenName(rs.getString("given_name"));
                row.setPositionId(rs.getInt("position_id"));
                row.setPositionName(rs.getString("position_name"));
                row.setPicture(rs.getString("user_profile_photo_url"));
                row.setCreatedOn(rs.getTimestamp("created_on"));
                row.setCreatedBy(rs.getString("created_by"));
                row.setUpdatedOn(rs.getTimestamp("updated_on"));
                row.setUpdatedBy(rs.getString("updated_by"));
                row.setStatus(rs.getInt("deleted"));
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

    public void updateScreenResolution(String resolution, User loggedInUser) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Auth_User\n");
        query.append("  SET\n");
        query.append("      screen_resolution = ?\n");
        query.append("  WHERE\n");
        query.append("      user_id = ?\n");
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, resolution);
                        ps.setInt(2, loggedInUser.getUserId());
                        return ps;
                    }
            );
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
        }
    }

    public Integer getUserIdByUsername(String username) {
        this.logger.debug("UserDao -> getUserIdByUsername: username=" + username);
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      user_id\n");
        query.append("  FROM\n");
        query.append("      CRC.Auth_User\n");
        query.append("  WHERE\n");
        query.append("      username = ?\n");
        query.append("      AND deleted = 0\n");
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{username}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public List<Role> getUserRoles(Integer userGuid) {
        this.logger.debug("UserDao -> getUserRoles: userGuid=" + userGuid);
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Auth_User_Role.role_id,\n");
        query.append("      role_name,\n");
        query.append("      authority\n");
        query.append("  FROM\n");
        query.append("      CRC.Auth_User_Role\n");
        query.append("      LEFT JOIN CRC.Auth_Role ON Auth_Role.role_id = Auth_User_Role.role_id AND Auth_Role.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      user_id = ?\n");
        query.append("      AND Auth_User_Role.deleted = 0\n");
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{userGuid}, new RoleRowMapper());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

}
