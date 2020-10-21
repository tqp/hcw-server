package com.timsanalytics.crc.main.dao.RowMappers;

import com.timsanalytics.crc.main.beans.CaseManager;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CaseManagerRowMapper implements RowMapper<CaseManager> {

    public CaseManager mapRow(ResultSet rs, int rowNum) throws SQLException {
        CaseManager row = new CaseManager();
        row.setCaseManagerId(rs.getInt("id"));
        row.setCaseManagerSurname(rs.getString("last_name"));
        row.setCaseManagerGivenName(rs.getString("first_name"));
        row.setCaseManagerPhone(rs.getString("phone"));
        row.setCaseManagerEmail(rs.getString("email"));
        return row;
    }
}
