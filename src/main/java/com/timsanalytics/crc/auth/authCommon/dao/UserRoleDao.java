package com.timsanalytics.crc.auth.authCommon.dao;

import com.timsanalytics.crc.auth.authCommon.beans.User;
import com.timsanalytics.crc.auth.authCommon.beans.UserRole;
import com.timsanalytics.crc.utils.PrintObjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
public class UserRoleDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Qualifier("mySqlAuthJdbcTemplate")
    private final JdbcTemplate mySqlAuthJdbcTemplate;

    @Autowired
    public UserRoleDao(JdbcTemplate mySqlAuthJdbcTemplate, PrintObjectService printObjectService) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
    }

    public Integer createUserRole(Integer userId, Integer roleId, Integer deleted, User loggedInUser) {
        this.logger.debug("UserRoleDao -> createUserRole: userId=" + userId + ", roleId=" + roleId);
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      USER_ROLE\n");
        query.append("  (\n");
        query.append("      servicesProvidedType,\n");
        query.append("      USER_GUID,\n");
        query.append("      ROLE_GUID,\n");
        query.append("      STATUS,\n");
        query.append("      CREATED_ON,\n");
        query.append("      CREATED_BY,\n");
        query.append("      UPDATED_ON,\n");
        query.append("      UPDATED_BY\n");
        query.append("  )\n");
        query.append("  VALUES\n");
        query.append("  (\n");
        query.append("      ?,\n");
        query.append("      ?,\n");
        query.append("      ?,\n");
        query.append("      ?,\n");
        query.append("      NOW(),\n");
        query.append("      ?,\n");
        query.append("      NOW(),\n");
        query.append("      ?\n");
        query.append("  )\n");
        this.logger.debug("SQL:\n" + query.toString());
        this.logger.debug("userId: " + userId);
        this.logger.debug("roleId: " + roleId);
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, userId);
                        ps.setInt(2, roleId);
                        ps.setInt(3, deleted);
                        ps.setString(4, loggedInUser.getUsername());
                        ps.setString(5, loggedInUser.getUsername());
                        return ps;
                    });
            return userId;
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public String updateUserRole(Integer userId, Integer roleId, String status, User loggedInUser) {
        this.logger.debug("UserRoleDao -> updateUserRole: userId=" + userId + ", roleId=" + roleId);
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE CRC.Auth_User_Role\n");
        query.append("      SET\n");
        query.append("          STATUS = ?,\n");
        query.append("          UPDATED_ON = NOW(),\n");
        query.append("          UPDATED_BY = ?\n");
        query.append("  WHERE\n");
        query.append("      USER_GUID = ?\n");
        query.append("      AND ROLE_GUID = ?\n");
        this.logger.debug("SQL:\n" + query.toString());
        this.logger.debug("userId: " + userId);
        this.logger.debug("roleId: " + roleId);
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, status);
                        ps.setString(2, loggedInUser.getUsername());
                        ps.setInt(3, userId);
                        ps.setInt(4, roleId);
                        return ps;
                    });
            return "success";
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public int[] insertUserRoleBatch(List<UserRole> userRoleList) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.Auth_User_Role\n");
        query.append("      (\n");
        query.append("          user_id,\n");
        query.append("          role_id,\n");
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
        query.append("          NOW(),\n");
        query.append("          ?,\n");
        query.append("          NOW(),\n");
        query.append("          ?,\n");
        query.append("          ?\n");
        query.append("      )\n");
        try {
            return this.mySqlAuthJdbcTemplate.batchUpdate(query.toString(),
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, userRoleList.get(i).getUserId());
                            ps.setInt(2, userRoleList.get(i).getRoleId());
                            ps.setInt(3, -1);
                            ps.setInt(4, -1);
                            ps.setInt(5, userRoleList.get(i).getStatus() ? 0 : 1);
                        }

                        @Override
                        public int getBatchSize() {
                            return userRoleList.size();
                        }
                    });
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            throw e;
        }
    }

    public int[] updateUserRoleBatch(List<UserRole> userRoleList) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Auth_User_Role\n");
        query.append("  SET\n");
        query.append("      user_id = ?,\n");
        query.append("      role_id = ?,\n");
        query.append("      deleted = ?\n");
        query.append("  WHERE\n");
        query.append("      user_role_id = ?\n");
        try {
            return this.mySqlAuthJdbcTemplate.batchUpdate(query.toString(),
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, userRoleList.get(i).getUserId());
                            ps.setInt(2, userRoleList.get(i).getRoleId());
                            ps.setInt(3, userRoleList.get(i).getStatus() ? 0 : 1);
                            ps.setInt(4, userRoleList.get(i).getUserRoleId());
                        }

                        @Override
                        public int getBatchSize() {
                            return userRoleList.size();
                        }
                    });
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public UserRole getUserRoleByUserIdAndRoleId(Integer userId, Integer roleId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      user_role_id,\n");
        query.append("      user_id,\n");
        query.append("      role_id\n");
        query.append("  FROM\n");
        query.append("      CRC.Auth_User_Role\n");
        query.append("  WHERE\n");
        query.append("      user_id = ?\n");
        query.append("      AND role_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        this.logger.trace("userId: " + userId);
        this.logger.trace("roleId: " + roleId);
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{userId, roleId},
                    (rs, rowNum) -> {
                        UserRole row = new UserRole();
                        row.setUserRoleId(rs.getInt("user_role_id"));
                        row.setUserId(rs.getInt("user_id"));
                        row.setRoleId(rs.getInt("role_id"));
                        return row;
                    });
        } catch (EmptyResultDataAccessException e) {
//            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }
}
