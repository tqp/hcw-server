package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.main.beans.Caregiver;
import com.timsanalytics.crc.main.beans.Relation;
import com.timsanalytics.crc.main.beans.Relationship;
import com.timsanalytics.crc.main.dao.RowMappers.CaregiverRowMapper;
import com.timsanalytics.crc.utils.GenerateUuidService;
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

    public Caregiver createCaregiver(Caregiver caregiver) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.Person\n");
        query.append("      (\n");
        query.append("          last_name,\n");
        query.append("          first_name,\n");
        query.append("          person_type_id,\n");
        query.append("          deleted\n");
        query.append("      )\n");
        query.append("      VALUES\n");
        query.append("      (\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          3,\n");
        query.append("          0\n");
        query.append("      )\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, caregiver.getCaregiverSurname());
                        ps.setString(2, caregiver.getCaregiverGivenName());
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
        query.append("      id,\n");
        query.append("      last_name,\n");
        query.append("      first_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Person\n");
        query.append("  WHERE\n");
        query.append("      person_type_id = 3 -- Caregiver\n");
        query.append("      AND deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      last_name,\n");
        query.append("      first_name \n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                Caregiver row = new Caregiver();
                row.setCaregiverId(rs.getInt("id"));
                row.setCaregiverSurname(rs.getString("last_name"));
                row.setCaregiverGivenName(rs.getString("first_name"));
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

        String sortColumn = serverSidePaginationRequest.getSortColumn();
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
        query.append("              last_name,\n");
        query.append("              first_name\n");
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
                row.setCaregiverId(rs.getInt("id"));
                row.setCaregiverSurname(rs.getString("last_name"));
                row.setCaregiverGivenName(rs.getString("first_name"));
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
        query.append("                  Person.id,\n");
        query.append("                  Person.first_name,\n");
        query.append("                  Person.last_name\n");
        query.append("              FROM\n");
        query.append("                  CRC.Person\n");
        query.append("              WHERE\n");
        query.append("              (\n");
        query.append("                      Person.Deleted = 0\n");
        query.append("                      AND Person.person_type_id = 3\n");
        query.append("                  AND\n");
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
            whereClause.append("                    UPPER(PERSON.PERSON_SURNAME) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                    OR");
            whereClause.append("                    UPPER(PERSON.PERSON_GIVEN_NAME) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                  )\n");
        } else {
            whereClause.append("                  (1=1)");
        }

        return whereClause.toString();
    }

    public List<Caregiver> getCaregiverListByStudentId(String studentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      RELATIONSHIP_GUID,\n");
        query.append("      STUDENT_GUID,\n");
        query.append("      RELATIONSHIP.PERSON_GUID,\n");
        query.append("      PERSON.PERSON_SURNAME,\n");
        query.append("      PERSON.PERSON_GIVEN_NAME,\n");
        query.append("      RELATIONSHIP.RELATIONSHIP_BLOOD_RELATIVE\n");
        query.append("  FROM\n");
        query.append("      HCW_DATA.RELATIONSHIP\n");
        query.append("      LEFT JOIN HCW_DATA.PERSON ON PERSON.PERSON_GUID = RELATIONSHIP.PERSON_GUID\n");
        query.append("  WHERE\n");
        query.append("      STUDENT_GUID = ?\n");
        query.append("      AND RELATIONSHIP_TYPE = 13\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{studentId}, (rs, rowNum) -> {
                Caregiver row = new Caregiver();
                row.setCaregiverId(rs.getInt("PERSON_GUID"));
                row.setCaregiverSurname(rs.getString("PERSON_SURNAME"));
                row.setCaregiverGivenName(rs.getString("PERSON_GIVEN_NAME"));
                row.setRelationshipBloodRelative(rs.getInt("RELATIONSHIP_BLOOD_RELATIVE") == 1);
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

    public Caregiver getCaregiverDetail(int caregiverId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      id,\n");
        query.append("      last_name,\n");
        query.append("      first_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Person\n");
        query.append("  WHERE\n");
        query.append("      id = ?\n");
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

    public Caregiver getCaregiverDetailByStudentId(int studentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("  Student.id,\n");
        query.append("          Relationship.person_id,\n");
        query.append("          Caregiver.last_name,\n");
        query.append("          Caregiver.first_name\n");
        query.append("  FROM\n");
        query.append("  CRC.Person Student\n");
        query.append("  LEFT JOIN CRC.StudentRelationship Relationship on Relationship.student_id =  Student.id AND Relationship.relationship_type_id = 13\n");
        query.append("  LEFT JOIN CRC.Person Caregiver ON Caregiver.id = Relationship.person_id\n");
        query.append("  WHERE\n");
        query.append("  Student.id = ?\n");
        query.append("  AND Student.person_type_id = 1\n");
        query.append("  ORDER BY Relationship.updated_on DESC\n");
        query.append("  LIMIT 1\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{studentId}, (rs, rowNum) -> {
                Caregiver row = new Caregiver();
                row.setCaregiverId(rs.getInt("person_id"));
                row.setCaregiverSurname(rs.getString("last_name"));
                row.setCaregiverGivenName(rs.getString("first_name"));
                return row;
            });
        } catch (EmptyResultDataAccessException e) {
            return new Caregiver();
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public Caregiver updateCaregiver(Caregiver caregiver) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Person\n");
        query.append("  SET\n");
        query.append("      last_name = ?,\n");
        query.append("      first_name = ?\n");
        query.append("  WHERE\n");
        query.append("      id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, caregiver.getCaregiverSurname());
                        ps.setString(2, caregiver.getCaregiverGivenName());
                        ps.setInt(3, caregiver.getCaregiverId());
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
        query.append("      CRC.Person\n");
        query.append("  SET\n");
        query.append("      deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      id = ?\n");
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

}
