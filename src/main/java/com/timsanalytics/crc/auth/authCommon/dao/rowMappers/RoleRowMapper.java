package com.timsanalytics.crc.auth.authCommon.dao.rowMappers;

import com.timsanalytics.crc.auth.authCommon.beans.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleRowMapper implements RowMapper<Role> {

    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role row = new Role();
        row.setRoleId(rs.getInt("role_id"));
        row.setRoleName(rs.getString("role_name"));
        row.setAuthority(rs.getString("authority"));
        return row;
    }
}
