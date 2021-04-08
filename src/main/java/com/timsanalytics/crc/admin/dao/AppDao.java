package com.timsanalytics.crc.admin.dao;

import com.timsanalytics.crc.main.dao.UtilsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AppDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;

    @Autowired
    public AppDao(JdbcTemplate mySqlAuthJdbcTemplate, UtilsDao utilsDao) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
    }

    public Integer getUserCount() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      COUNT(*)\n");
        query.append("  FROM\n");
        query.append("      CRC.Auth_User\n");
        query.append("  WHERE\n");
        query.append("      deleted = 0\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            System.out.println(this.mySqlAuthJdbcTemplate.getDataSource().getConnection());
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }
}
