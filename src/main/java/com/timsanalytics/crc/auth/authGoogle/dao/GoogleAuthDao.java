package com.timsanalytics.crc.auth.authGoogle.dao;

import com.timsanalytics.crc.auth.authCommon.beans.User;
import com.timsanalytics.crc.auth.authGoogle.beans.GoogleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

@Service
public class GoogleAuthDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Qualifier("mySqlAuthJdbcTemplate")
    private final JdbcTemplate mySqlAuthJdbcTemplate;

    public GoogleAuthDao(JdbcTemplate mySqlAuthJdbcTemplate) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
    }

    public void updateUserRecordFromGoogleAuth(User appUser, GoogleUser googleUser) {
        this.logger.trace("AppUserDao -> updateUserRecord: userId=" + appUser.getUserId());
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Auth_User\n");
        query.append("  SET\n");
        query.append("      user_profile_photo_url = ?,\n");
        query.append("      last_login = NOW(),\n");
        query.append("      login_count =\n");
        query.append("          CASE\n");
        query.append("              WHEN login_count IS NULL THEN 1\n");
        query.append("              ELSE login_count + 1\n");
        query.append("          END\n");
        query.append("  WHERE\n");
        query.append("      user_id = ?\n");
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, googleUser.getPicture());
                        ps.setInt(2, appUser.getUserId());
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
