package com.timsanalytics.crc.main.dao.RowMappers;

import com.timsanalytics.crc.main.beans.Sponsor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SponsorRowMapper implements RowMapper<Sponsor> {

    public Sponsor mapRow(ResultSet rs, int rowNum) throws SQLException {
        Sponsor row = new Sponsor();
        row.setSponsorId(rs.getInt("sponsor_id"));
        row.setSponsorSurname(rs.getString("surname"));
        row.setSponsorGivenName(rs.getString("given_name"));
        row.setSponsorAffiliatedChurch(rs.getString("affiliated_church"));
        return row;
    }
}
