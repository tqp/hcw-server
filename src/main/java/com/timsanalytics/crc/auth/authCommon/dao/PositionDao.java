package com.timsanalytics.crc.auth.authCommon.dao;

import com.timsanalytics.crc.auth.authCommon.beans.Position;
import com.timsanalytics.crc.auth.authCommon.dao.rowMappers.PositionRowMapper;
import com.timsanalytics.crc.main.dao.UtilsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;

    @Autowired
    public PositionDao(JdbcTemplate mySqlAuthJdbcTemplate) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
    }

    public List<Position> getPositionList() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      position_id,\n");
        query.append("      position_name,\n");
        query.append("      role_ids\n");
        query.append("  FROM\n");
        query.append("      CRC.Auth_Position\n");
        query.append("  WHERE\n");
        query.append("      deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      sort_order,\n");
        query.append("      position_name\n");
        this.logger.debug("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, new PositionRowMapper());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }
}
