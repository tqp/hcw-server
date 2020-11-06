package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.main.beans.Caregiver;
import com.timsanalytics.crc.main.dao.RowMappers.CaregiverRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class CaregiverDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;
    private final UtilsDao utilsDao;

    @Autowired
    public CaregiverDao(JdbcTemplate mySqlAuthJdbcTemplate, UtilsDao utilsDao) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.utilsDao = utilsDao;
    }

    // BASIC CRUD

    public Caregiver createCaregiver(Caregiver caregiver) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.Person_Caregiver\n");
        query.append("      (\n");
        query.append("          surname,\n");
        query.append("          given_name,\n");
        query.append("          address,\n");
        query.append("          phone,\n");
        query.append("          email,\n");
        query.append("          deleted\n");
        query.append("      )\n");
        query.append("      VALUES\n");
        query.append("      (\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          0\n");
        query.append("      )\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, caregiver.getCaregiverSurname());
                        ps.setString(2, caregiver.getCaregiverGivenName());
                        ps.setString(3, caregiver.getCaregiverAddress());
                        ps.setString(4, caregiver.getCaregiverPhone());
                        ps.setString(5, caregiver.getCaregiverEmail());
                        return ps;
                    }
            );
            int lastInsertId = this.utilsDao.getLastInsertId();
            this.logger.debug("New Caregiver ID: " + lastInsertId);
            return this.getCaregiverDetail(lastInsertId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public List<Caregiver> getCaregiverList() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      caregiver_id,\n");
        query.append("      surname,\n");
        query.append("      given_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Person_Caregiver\n");
        query.append("  WHERE\n");
        query.append("      deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      surname,\n");
        query.append("      given_name\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                Caregiver row = new Caregiver();
                row.setCaregiverId(rs.getInt("caregiver_id"));
                row.setCaregiverSurname(rs.getString("surname"));
                row.setCaregiverGivenName(rs.getString("given_name"));
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

    public int getCaregiverList_SSP_TotalRecords(ServerSidePaginationRequest<Caregiver> serverSidePaginationRequest) {
        StringBuilder query = new StringBuilder();
        query.append("          SELECT\n");
        query.append("              COUNT(*)\n");
        query.append("          FROM\n");
        query.append("          -- ROOT QUERY\n");
        query.append("          (\n");
        query.append(getCaregiverList_SSP_RootQuery(serverSidePaginationRequest));
        query.append("          ) AS ROOT_QUERY\n");
        query.append("          -- END ROOT QUERY\n");
        try {
            Integer count = this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{}, Integer.class);
            return count == null ? 0 : count;
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return 0;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return 0;
        }
    }

    public List<Caregiver> getCaregiverList_SSP(ServerSidePaginationRequest<Caregiver> serverSidePaginationRequest) {
        int pageStart = (serverSidePaginationRequest.getPageIndex()) * serverSidePaginationRequest.getPageSize();
        int pageSize = serverSidePaginationRequest.getPageSize();

        String defaultSortField = "surname";
        String sortColumn = serverSidePaginationRequest.getSortColumn() != null ? serverSidePaginationRequest.getSortColumn() : defaultSortField;
        String sortDirection = serverSidePaginationRequest.getSortDirection();

        StringBuilder query = new StringBuilder();
        query.append("  -- PAGINATION QUERY\n");
        query.append("  SELECT\n");
        query.append("      FILTER_SORT_QUERY.*\n");
        query.append("  FROM\n");

        query.append("      -- FILTER/SORT QUERY\n");
        query.append("      (\n");
        query.append("          SELECT\n");
        query.append("              *\n");
        query.append("          FROM\n");

        query.append("          -- ROOT QUERY\n");
        query.append("          (\n");
        query.append(getCaregiverList_SSP_RootQuery(serverSidePaginationRequest));
        query.append("          ) AS ROOT_QUERY\n");
        query.append("          -- END ROOT QUERY\n");

        query.append("          ORDER BY\n");
        query.append(sortColumn).append(" ").append(sortDirection.toUpperCase()).append(",\n");
        query.append("              surname,\n");
        query.append("              given_name\n");
        query.append("      ) AS FILTER_SORT_QUERY\n");
        query.append("      -- END FILTER/SORT QUERY\n");

        query.append("  LIMIT ?, ?\n");
        query.append("  -- END PAGINATION QUERY\n");

        this.logger.trace("SQL:\n" + query.toString());
        this.logger.trace("pageStart=" + pageStart + ", pageSize=" + pageSize);

        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{
                    pageStart,
                    pageSize
            }, (rs, rowNum) -> {
                Caregiver row = new Caregiver();
                row.setCaregiverId(rs.getInt("caregiver_id"));
                row.setCaregiverSurname(rs.getString("surname"));
                row.setCaregiverGivenName(rs.getString("given_name"));
                row.setCaregiverPhone(rs.getString("phone"));
                row.setCaregiverEmail(rs.getString("email"));
                row.setCaregiverAddress(rs.getString("address"));
                row.setStudentCount(rs.getInt("student_count"));
                return row;
            });
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            this.logger.error("pageStart=" + pageSize + ", pageSize=" + pageSize);
            return null;
        }
    }

    private String getCaregiverList_SSP_RootQuery(ServerSidePaginationRequest<Caregiver> serverSidePaginationRequest) {
        //noinspection StringBufferReplaceableByString
        StringBuilder query = new StringBuilder();
        query.append("              SELECT\n");
        query.append("                  caregiver_id,\n");
        query.append("                  surname,\n");
        query.append("                  given_name,\n");
        query.append("                  phone,\n");
        query.append("                  email,\n");
        query.append("                  address,\n");
        query.append("                  (\n");
        query.append("                      SELECT\n");
        query.append("                          COUNT(*)\n");
        query.append("                      FROM\n");
        query.append("                          CRC.Rel_Student_Caregiver\n");
        query.append("                      WHERE\n");
        query.append("                          Rel_Student_Caregiver.caregiver_id = Person_Caregiver.caregiver_id\n");
        query.append("                          AND deleted = 0\n");
        query.append("                  ) as student_count\n");
        query.append("              FROM\n");
        query.append("                  CRC.Person_Caregiver\n");
        query.append("              WHERE\n");
        query.append("              (\n");
        query.append("                  deleted = 0\n");
        query.append("                  AND\n");
        query.append("                  ");
        query.append(getCaregiverList_SSP_AdditionalWhereClause(serverSidePaginationRequest));
        query.append("              )");
        return query.toString();
    }

    private String getCaregiverList_SSP_AdditionalWhereClause(ServerSidePaginationRequest<Caregiver> serverSidePaginationRequest) {
        StringBuilder whereClause = new StringBuilder();
        String nameFilter = serverSidePaginationRequest.getNameFilter() != null ? serverSidePaginationRequest.getNameFilter() : "";

        // NAME FILTER CLAUSE
        if (!"".equalsIgnoreCase(nameFilter)) {
            whereClause.append("                  (\n");
            whereClause.append("                    UPPER(Person_Caregiver.surname) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                    OR");
            whereClause.append("                    UPPER(Person_Caregiver.given_name) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                  )\n");
        } else {
            whereClause.append("                  (1=1)");
        }

        return whereClause.toString();
    }

    public Caregiver getCaregiverDetail(int caregiverId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      caregiver_id,\n");
        query.append("      surname,\n");
        query.append("      given_name,\n");
        query.append("      address,\n");
        query.append("      phone,\n");
        query.append("      email\n");
        query.append("  FROM\n");
        query.append("      CRC.Person_Caregiver\n");
        query.append("  WHERE\n");
        query.append("      caregiver_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{caregiverId}, new CaregiverRowMapper());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public Caregiver updateCaregiver(Caregiver caregiver) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Person_Caregiver\n");
        query.append("  SET\n");
        query.append("      surname = ?,\n");
        query.append("      given_name = ?,\n");
        query.append("      address = ?,\n");
        query.append("      phone = ?,\n");
        query.append("      email = ?\n");
        query.append("  WHERE\n");
        query.append("      caregiver_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, caregiver.getCaregiverSurname());
                        ps.setString(2, caregiver.getCaregiverGivenName());
                        ps.setString(3, caregiver.getCaregiverAddress());
                        ps.setString(4, caregiver.getCaregiverPhone());
                        ps.setString(5, caregiver.getCaregiverEmail());
                        ps.setInt(6, caregiver.getCaregiverId());
                        return ps;
                    }
            );
            return this.getCaregiverDetail(caregiver.getCaregiverId());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public KeyValue deleteCaregiver(String caregiverGuid) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Person_Caregiver\n");
        query.append("  SET\n");
        query.append("      deleted = 1\n");
        query.append("  WHERE\n");
        query.append("      caregiver_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        this.logger.trace("id=" + caregiverGuid);
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, caregiverGuid);
                        return ps;
                    }
            );
            return new KeyValue("caregiverGuid", caregiverGuid);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    // JOINED QUERIES

    public Caregiver getCaregiverDetailByStudentId(int studentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Person_Caregiver.caregiver_id,\n");
        query.append("      Person_Caregiver.surname,\n");
        query.append("      Person_Caregiver.given_name,\n");
        query.append("      Person_Caregiver.address,\n");
        query.append("      Person_Caregiver.phone,\n");
        query.append("      Rel_Student_Caregiver.start_date,\n");
        query.append("      Ref_Tier_Type.tier_type_id AS tier_type_id,\n");
        query.append("      Ref_Tier_Type.name AS tier_type_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Rel_Student_Caregiver\n");
        query.append("      LEFT JOIN CRC.Person_Caregiver ON Person_Caregiver.caregiver_id = Rel_Student_Caregiver.caregiver_id AND Person_Caregiver.deleted = 0\n");
        query.append("      LEFT JOIN CRC.Ref_Tier_Type ON Ref_Tier_Type.tier_type_id = Rel_Student_Caregiver.tier_type_id AND Ref_Tier_Type.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      student_id = ?\n");
        query.append("      AND Rel_Student_Caregiver.deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      start_date DESC\n");
        query.append("  LIMIT 0, 1\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{studentId}, (rs, rowNum) -> {
                Caregiver row = new Caregiver();
                row.setCaregiverId(rs.getInt("caregiver_id"));
                row.setCaregiverSurname(rs.getString("surname"));
                row.setCaregiverGivenName(rs.getString("given_name"));
                row.setCaregiverAddress(rs.getString("address"));
                row.setCaregiverPhone(rs.getString("phone"));
                row.setRelationshipStartDate(rs.getString("start_date"));
                row.setRelationshipTierTypeID(rs.getInt("tier_type_id"));
                row.setRelationshipTierTypeName(rs.getString("tier_type_name"));
                return row;
            });
        } catch (EmptyResultDataAccessException e) {
            return new Caregiver();
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public List<Caregiver> getCaregiverWithLoanList() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Person_Caregiver.caregiver_id,\n");
        query.append("      Person_Caregiver.surname,\n");
        query.append("      Person_Caregiver.given_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Microfinance_Loan\n");
        query.append("      LEFT JOIN CRC.Person_Caregiver ON Person_Caregiver.caregiver_id = Microfinance_Loan.caregiver_id AND Person_Caregiver.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      Microfinance_Loan.deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      Person_Caregiver.surname,\n");
        query.append("      Person_Caregiver.given_name\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                Caregiver row = new Caregiver();
                row.setCaregiverId(rs.getInt("caregiver_id"));
                row.setCaregiverSurname(rs.getString("surname"));
                row.setCaregiverGivenName(rs.getString("given_name"));
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
