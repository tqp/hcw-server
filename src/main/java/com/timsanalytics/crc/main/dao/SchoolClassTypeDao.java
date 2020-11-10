package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.main.beans.ProgramStatusPackage;
import com.timsanalytics.crc.main.beans.SchoolClassType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolClassTypeDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;

    @Autowired
    public SchoolClassTypeDao(JdbcTemplate mySqlAuthJdbcTemplate) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
    }

    public List<SchoolClassType> getSchoolClassTypeChildList(Integer parentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      school_class_type_id,\n");
        query.append("      name\n");
        query.append("  FROM\n");
        query.append("      CRC.Ref_School_Class_Type\n");
        query.append("  WHERE\n");
        query.append("      parent_id = ?\n");
        query.append("      AND deleted = 0\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{parentId}, (rs, rowNum) -> {
                SchoolClassType row = new SchoolClassType();
                row.setSchoolClassTypeId(rs.getInt("school_class_type_id"));
                row.setSchoolClassTypeName(rs.getString("name"));
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
