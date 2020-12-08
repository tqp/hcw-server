package com.timsanalytics.crc.auth.authCommon.dao;

import com.timsanalytics.crc.auth.authCommon.beans.Role;
import com.timsanalytics.crc.auth.authCommon.dao.rowMappers.RoleRowMapper;
import com.timsanalytics.crc.main.beans.Student;
import com.timsanalytics.crc.main.dao.UtilsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;
    private final UtilsDao utilsDao;

    @Autowired
    public RoleDao(JdbcTemplate mySqlAuthJdbcTemplate, UtilsDao utilsDao) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.utilsDao = utilsDao;
    }

    public List<Role> getRoleList() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      role_id,\n");
        query.append("      name,\n");
        query.append("      authority\n");
        query.append("  FROM\n");
        query.append("      CRC.Auth_Role\n");
        query.append("  WHERE\n");
        query.append("      deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      sort_order,\n");
        query.append("      name\n");
        this.logger.debug("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, new RoleRowMapper());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public List<Role> getRoleListByUserId(Integer userId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Auth_User_Role.role_id,\n");
        query.append("      name,\n");
        query.append("      authority\n");
        query.append("  FROM\n");
        query.append("      CRC.Auth_User_Role\n");
        query.append("      LEFT JOIN CRC.Auth_Role ON Auth_Role.role_id = Auth_User_Role.role_id AND Auth_Role.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      user_id = ?\n");
        query.append("      AND Auth_User_Role.deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      sort_order,\n");
        query.append("      name\n");
        this.logger.debug("SQL:\n" + query.toString());
        this.logger.debug("userId: " + userId);
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{userId}, new RoleRowMapper());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }
}
