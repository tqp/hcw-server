package com.timsanalytics.crc.auth.authCommon.dao.rowMappers;

import com.timsanalytics.crc.auth.authCommon.beans.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
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
        row.setPositionId(rs.getInt("position_name"));
        row.setPicture(rs.getString("user_profile_photo_url"));

        row.setCreatedOn(rs.getTimestamp("created_on"));
        row.setCreatedBy(rs.getString("created_by"));
        row.setUpdatedOn(rs.getTimestamp("updated_on"));
        row.setUpdatedBy(rs.getString("updated_by"));
        row.setStatus(rs.getInt("deleted"));
        return row;
    }
}
