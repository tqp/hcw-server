package com.timsanalytics.crc.main.dao.types;

import com.timsanalytics.crc.main.beans.types.PersonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonTypeDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;

    @Autowired
    public PersonTypeDao(JdbcTemplate mySqlAuthJdbcTemplate) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
    }

    public List<PersonType> getPersonTypeList() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      id,\n");
        query.append("      name\n");
        query.append("  FROM\n");
        query.append("      CRC.PersonType\n");
        query.append("  WHERE\n");
        query.append("      deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      name\n");
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                PersonType row = new PersonType();
                row.setPersonTypeId(rs.getInt("id"));
                row.setPersonTypeName(rs.getString("name"));
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

