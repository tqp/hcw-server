package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.main.beans.Sponsor;
import com.timsanalytics.crc.main.dao.RowMappers.SponsorRowMapper;
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
public class SponsorDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;
    private final UtilsDao utilsDao;

    @Autowired
    public SponsorDao(JdbcTemplate mySqlAuthJdbcTemplate, UtilsDao utilsDao) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.utilsDao = utilsDao;
    }

    public Sponsor createSponsor(Sponsor sponsor) {
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
        query.append("          5,\n");
        query.append("          0\n");
        query.append("      )\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, sponsor.getSponsorSurname());
                        ps.setString(2, sponsor.getSponsorGivenName());
                        return ps;
                    }
            );
            int lastInsertId = this.utilsDao.getLastInsertId();
            this.logger.debug("New Sponsor ID: " + lastInsertId);
            return this.getSponsorDetail(lastInsertId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public List<Sponsor> getSponsorList() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      id,\n");
        query.append("      last_name,\n");
        query.append("      first_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Person\n");
        query.append("  WHERE\n");
        query.append("      person_type_id = 5 -- Sponsor\n");
        query.append("      AND deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      last_name,\n");
        query.append("      first_name \n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                Sponsor row = new Sponsor();
                row.setSponsorId(rs.getInt("id"));
                row.setSponsorSurname(rs.getString("last_name"));
                row.setSponsorGivenName(rs.getString("first_name"));
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

    public int getSponsorList_SSP_TotalRecords(ServerSidePaginationRequest<Sponsor> serverSidePaginationRequest) {
        StringBuilder query = new StringBuilder();
        query.append("          SELECT\n");
        query.append("              COUNT(*)\n");
        query.append("          FROM\n");
        query.append("          -- ROOT QUERY\n");
        query.append("          (\n");
        query.append(getSponsorList_SSP_RootQuery(serverSidePaginationRequest));
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

    public List<Sponsor> getSponsorList_SSP(ServerSidePaginationRequest<Sponsor> serverSidePaginationRequest) {
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
        query.append(getSponsorList_SSP_RootQuery(serverSidePaginationRequest));
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
                Sponsor row = new Sponsor();
                row.setSponsorId(rs.getInt("id"));
                row.setSponsorSurname(rs.getString("last_name"));
                row.setSponsorGivenName(rs.getString("first_name"));
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

    private String getSponsorList_SSP_RootQuery(ServerSidePaginationRequest<Sponsor> serverSidePaginationRequest) {
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
        query.append("                      AND Person.person_type_id = 5 -- Sponsor\n");
        query.append("                  AND\n");
        query.append(getSponsorList_SSP_AdditionalWhereClause(serverSidePaginationRequest));
        query.append("              )");
        return query.toString();
    }

    private String getSponsorList_SSP_AdditionalWhereClause(ServerSidePaginationRequest<Sponsor> serverSidePaginationRequest) {
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

    public List<Sponsor> getSponsorListByStudentId(String studentId) {
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
        query.append("      AND RELATIONSHIP_TYPE = 14 -- Sponsor\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{studentId}, (rs, rowNum) -> {
                Sponsor row = new Sponsor();
                row.setSponsorId(rs.getInt("PERSON_GUID"));
                row.setSponsorSurname(rs.getString("PERSON_SURNAME"));
                row.setSponsorGivenName(rs.getString("PERSON_GIVEN_NAME"));
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

    public Sponsor getSponsorDetail(int sponsorId) {
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
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{sponsorId}, new SponsorRowMapper());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public Sponsor getSponsorDetailByStudentId(int studentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("  Student.id,\n");
        query.append("          Relationship.person_id,\n");
        query.append("          Sponsor.last_name,\n");
        query.append("          Sponsor.first_name\n");
        query.append("  FROM\n");
        query.append("  CRC.Person Student\n");
        query.append("  LEFT JOIN CRC.StudentRelationship Relationship on Relationship.student_id =  Student.id AND Relationship.relationship_type_id = 14 -- Sponsor\n");
        query.append("  LEFT JOIN CRC.Person Sponsor ON Sponsor.id = Relationship.person_id\n");
        query.append("  WHERE\n");
        query.append("  Student.id = ?\n");
        query.append("  AND Student.person_type_id = 1\n");
        query.append("  ORDER BY Relationship.updated_on DESC\n");
        query.append("  LIMIT 1\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{studentId}, (rs, rowNum) -> {
                Sponsor row = new Sponsor();
                row.setSponsorId(rs.getInt("person_id"));
                row.setSponsorSurname(rs.getString("last_name"));
                row.setSponsorGivenName(rs.getString("first_name"));
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

    public Sponsor updateSponsor(Sponsor sponsor) {
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
                        ps.setString(1, sponsor.getSponsorSurname());
                        ps.setString(2, sponsor.getSponsorGivenName());
                        ps.setInt(3, sponsor.getSponsorId());
                        return ps;
                    }
            );
            return this.getSponsorDetail(sponsor.getSponsorId());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public KeyValue deleteSponsor(String sponsorGuid) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Person\n");
        query.append("  SET\n");
        query.append("      deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        this.logger.trace("PERSON_GUID=" + sponsorGuid);
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, sponsorGuid);
                        return ps;
                    }
            );
            return new KeyValue("sponsorGuid", sponsorGuid);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

}
