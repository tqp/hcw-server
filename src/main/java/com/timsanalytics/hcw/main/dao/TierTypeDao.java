package com.timsanalytics.hcw.main.dao;

import com.timsanalytics.hcw.main.beans.TierType;
import com.timsanalytics.hcw.utils.GenerateUuidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TierTypeDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;
    private final GenerateUuidService generateUuidService;

    @Autowired
    public TierTypeDao(JdbcTemplate mySqlAuthJdbcTemplate, GenerateUuidService generateUuidService) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.generateUuidService = generateUuidService;
    }

    public List<TierType> getTierTypeList() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      TIER_TYPE_GUID,\n");
        query.append("      TIER_TYPE_NAME\n");
        query.append("  FROM\n");
        query.append("      HCW_DATA.TIER_TYPE\n");
        query.append("  WHERE\n");
        query.append("      STATUS = 'Active'\n");
        query.append("  ORDER BY\n");
        query.append("      TIER_TYPE_NAME\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                TierType row = new TierType();
                row.setTierTypeGuid(rs.getString("TIER_TYPE_GUID"));
                row.setTierTypeName(rs.getString("TIER_TYPE_NAME"));
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
