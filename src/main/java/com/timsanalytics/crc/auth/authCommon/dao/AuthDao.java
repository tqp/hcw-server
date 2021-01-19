package com.timsanalytics.crc.auth.authCommon.dao;

import com.timsanalytics.crc.auth.authCommon.beans.Role;
import com.timsanalytics.crc.auth.authCommon.beans.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Qualifier("mySqlAuthJdbcTemplate")
    private final JdbcTemplate mySqlAuthJdbcTemplate;

    public AuthDao(JdbcTemplate mySqlAuthJdbcTemplate) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
    }

    public User getUserForAuthentication(String username) {
        this.logger.debug("getUserForAuthentication: " + username);
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      user_id,\n");
        query.append("      username,\n");
        query.append("      password,\n");
        query.append("      last_login,\n");
        query.append("      login_count,\n");
        query.append("      deleted\n");
        query.append("  FROM\n");
        query.append("      CRC.Auth_User\n");
        query.append("  WHERE\n");
        query.append("      username = ?\n");
        query.append("      AND deleted = 0\n");
        this.logger.debug("SQL:\n" + query.toString());
        this.logger.debug("username: " + username);
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{username},
                    (rs, rowNum) -> {
                        User row = new User();
                        row.setUserId(rs.getInt("user_id"));
                        row.setUsername(rs.getString("username"));
                        row.setPassword(rs.getString("password"));
                        row.setLastLogin(rs.getString("last_login"));
                        row.setLoginCount(rs.getInt("login_count"));
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

    public List<Role> getRolesByUser(Integer userGuid) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Auth_User_Role.role_id,\n");
        query.append("      role_name,\n");
        query.append("      authority,\n");
        query.append("      Auth_User_Role.created_on,\n");
        query.append("      Auth_User_Role.created_by,\n");
        query.append("      Auth_User_Role.updated_on,\n");
        query.append("      Auth_User_Role.updated_by,\n");
        query.append("      Auth_User_Role.deleted\n");
        query.append("  FROM\n");
        query.append("      CRC.Auth_User_Role\n");
        query.append("      LEFT JOIN CRC.Auth_Role ON Auth_Role.role_id = Auth_User_Role.role_id AND Auth_Role.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      user_id = ?\n");
        this.logger.debug("SQL:\n" + query.toString());
        this.logger.debug("userGuid: " + userGuid);
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{userGuid},
                    (rs, rowNum) -> {
                        Role row = new Role();
                        row.setRoleId(rs.getInt("role_id"));
                        row.setRoleName(rs.getString("role_name"));
                        row.setAuthority(rs.getString("authority"));
                        row.setCreatedOn(rs.getTimestamp("created_on"));
                        row.setCreatedBy(rs.getString("created_by"));
                        row.setUpdatedOn(rs.getTimestamp("updated_on"));
                        row.setUpdatedBy(rs.getString("updated_by"));
                        row.setDeleted(rs.getInt("deleted"));
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
