package com.timsanalytics.crc.main.dao.RowMappers;

import com.timsanalytics.crc.main.beans.CaseManager;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CaseManagerRowMapper implements RowMapper<CaseManager> {

    public CaseManager mapRow(ResultSet rs, int rowNum) throws SQLException {
        CaseManager row = new CaseManager();
        row.setCaseManagerGuid(rs.getString("id"));
        row.setCaseManagerSurname(rs.getString("last_name"));
        row.setCaseManagerGivenName(rs.getString("first_name"));
        return row;
    }
}
