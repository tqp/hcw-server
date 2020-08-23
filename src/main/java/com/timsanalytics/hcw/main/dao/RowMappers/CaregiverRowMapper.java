package com.timsanalytics.hcw.main.dao.RowMappers;

import com.timsanalytics.hcw.main.beans.Caregiver;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CaregiverRowMapper implements RowMapper<Caregiver> {

    public Caregiver mapRow(ResultSet rs, int rowNum) throws SQLException {
        Caregiver row = new Caregiver();
        row.setCaregiverGuid(rs.getString("PERSON_GUID"));
        row.setCaregiverSurname(rs.getString("PERSON_SURNAME"));
        row.setCaregiverGivenName(rs.getString("PERSON_GIVEN_NAME"));
        row.setCaregiverGender(rs.getString("PERSON_GENDER"));
        row.setStatus(rs.getString("STATUS"));
        return row;
    }
}
