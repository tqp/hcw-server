package com.timsanalytics.crc.main.dao.RowMappers;

import com.timsanalytics.crc.main.beans.Caregiver;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CaregiverRowMapper implements RowMapper<Caregiver> {

    public Caregiver mapRow(ResultSet rs, int rowNum) throws SQLException {
        Caregiver row = new Caregiver();
        row.setCaregiverId(rs.getInt("caregiver_id"));
        row.setCaregiverSurname(rs.getString("surname"));
        row.setCaregiverGivenName(rs.getString("given_name"));
        row.setCaregiverAddress(rs.getString("address"));
        row.setCaregiverPhone(rs.getString("phone"));
        row.setCaregiverEmail(rs.getString("email"));
        return row;
    }
}
