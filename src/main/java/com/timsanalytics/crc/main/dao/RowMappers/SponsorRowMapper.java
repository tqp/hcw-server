package com.timsanalytics.crc.main.dao.RowMappers;

import com.timsanalytics.crc.main.beans.Sponsor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SponsorRowMapper implements RowMapper<Sponsor> {

    public Sponsor mapRow(ResultSet rs, int rowNum) throws SQLException {
        Sponsor row = new Sponsor();
        row.setSponsorId(rs.getInt("id"));
        row.setSponsorSurname(rs.getString("last_name"));
        row.setSponsorGivenName(rs.getString("first_name"));
        return row;
    }
}
