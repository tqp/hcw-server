package com.timsanalytics.crc.auth.authInternal.dao;

import com.timsanalytics.crc.auth.authCommon.beans.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

@Service
public class InternalAuthDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Qualifier("mySqlAuthJdbcTemplate")
    private final JdbcTemplate mySqlAuthJdbcTemplate;

    public InternalAuthDao(JdbcTemplate mySqlAuthJdbcTemplate) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
    }

    public void updateUserRecordFromInternalAuth(User appUser) {
        this.logger.trace("InternalAuthDao -> updateUserRecord: userId=" + appUser.getUserId());
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Auth_User\n");
        query.append("  SET\n");
        query.append("      last_login = NOW(),\n");
        query.append("      login_count =\n");
        query.append("          CASE\n");
        query.append("              WHEN login_count IS NULL THEN 1\n");
        query.append("              ELSE login_count + 1\n");
        query.append("          END\n");
        query.append("  WHERE\n");
        query.append("      user_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        this.logger.trace("userGuid: " + appUser.getUserId());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, appUser.getUserId());
                        return ps;
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
        }
    }
}
