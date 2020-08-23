package com.timsanalytics.hcw.main.dao.RowMappers;

import com.timsanalytics.hcw.main.beans.CaseManager;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CaseManagerRowMapper implements RowMapper<CaseManager> {

    public CaseManager mapRow(ResultSet rs, int rowNum) throws SQLException {
        CaseManager row = new CaseManager();
        row.setCaseManagerGuid(rs.getString("PERSON_GUID"));
        row.setCaseManagerSurname(rs.getString("PERSON_SURNAME"));
        row.setCaseManagerGivenName(rs.getString("PERSON_GIVEN_NAME"));
        row.setCaseManagerGender(rs.getString("PERSON_GENDER"));
        row.setStatus(rs.getString("STATUS"));
        return row;
    }
}
