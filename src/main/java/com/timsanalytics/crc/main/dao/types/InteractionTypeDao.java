package com.timsanalytics.crc.main.dao.types;

import com.timsanalytics.crc.main.beans.types.InteractionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InteractionTypeDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;

    @Autowired
    public InteractionTypeDao(JdbcTemplate mySqlAuthJdbcTemplate) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
    }

    public List<InteractionType> getInteractionTypeList() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      interaction_type_id,\n");
        query.append("      interaction_type_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Ref_Interaction_Type\n");
        query.append("  WHERE\n");
        query.append("      deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      sort_order,\n");
        query.append("      interaction_type_name\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                InteractionType row = new InteractionType();
                row.setInteractionTypeId(rs.getInt("interaction_type_id"));
                row.setInteractionTypeName(rs.getString("name"));
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
