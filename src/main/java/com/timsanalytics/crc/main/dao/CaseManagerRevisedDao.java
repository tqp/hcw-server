package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.main.beans.CaseManager;
import com.timsanalytics.crc.main.beans.CaseManagerRevised;
import com.timsanalytics.crc.main.dao.RowMappers.CaseManagerRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class CaseManagerRevisedDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;
    private final UtilsDao utilsDao;

    @Autowired
    public CaseManagerRevisedDao(JdbcTemplate mySqlAuthJdbcTemplate, UtilsDao utilsDao) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.utilsDao = utilsDao;
    }

    // BASIC CRUD

    public List<CaseManagerRevised> getCaseManagerRevisedList() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Auth_User.user_id,\n");
        query.append("      Auth_User.username,\n");
        query.append("      Auth_User.surname,\n");
        query.append("      Auth_User.given_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Auth_User\n");
        query.append("      LEFT JOIN CRC.Auth_User_Role ON Auth_User_Role.user_id = Auth_User.user_id AND Auth_User_Role.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      Auth_User.deleted = 0\n");
        query.append("      AND Auth_User_Role.role_id = 5\n");
        query.append("  ORDER BY\n");
        query.append("      Auth_User.surname,\n");
        query.append("      Auth_User.given_name\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                CaseManagerRevised row = new CaseManagerRevised();
                row.setCaseManagerId(rs.getInt("user_id"));
                row.setCaseManagerSurname(rs.getString("surname"));
                row.setCaseManagerGivenName(rs.getString("given_name"));
                return row;
            });
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }
}
