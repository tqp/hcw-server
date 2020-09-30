package com.timsanalytics.crc.main.dao.RowMappers;

import com.timsanalytics.crc.main.beans.Caregiver;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CaregiverRowMapper implements RowMapper<Caregiver> {

    public Caregiver mapRow(ResultSet rs, int rowNum) throws SQLException {
        Caregiver row = new Caregiver();
        row.setCaregiverGuid(rs.getString("id"));
        row.setCaregiverSurname(rs.getString("last_name"));
        row.setCaregiverGivenName(rs.getString("first_name"));
        return row;
    }
}