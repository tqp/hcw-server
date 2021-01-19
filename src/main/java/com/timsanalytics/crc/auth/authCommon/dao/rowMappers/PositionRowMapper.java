package com.timsanalytics.crc.auth.authCommon.dao.rowMappers;

import com.timsanalytics.crc.auth.authCommon.beans.Position;
import com.timsanalytics.crc.auth.authCommon.beans.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PositionRowMapper implements RowMapper<Position> {

    public Position mapRow(ResultSet rs, int rowNum) throws SQLException {
        Position row = new Position();
        row.setPositionId(rs.getInt("position_id"));
        row.setPositionName(rs.getString("position_name"));
        row.setRoleIds(rs.getString("role_ids"));
        return row;
    }
}
