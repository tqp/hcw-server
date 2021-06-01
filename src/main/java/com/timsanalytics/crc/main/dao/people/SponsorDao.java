package com.timsanalytics.crc.main.dao.people;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.main.beans.Sponsor;
import com.timsanalytics.crc.main.beans.Student;
import com.timsanalytics.crc.main.dao.RowMappers.SponsorRowMapper;
import com.timsanalytics.crc.main.dao.UtilsDao;
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

    // BASIC CRUD

    public Sponsor createSponsor(Sponsor sponsor) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.Person_Sponsor\n");
        query.append("      (\n");
        query.append("          surname,\n");
        query.append("          given_name,\n");
        query.append("          affiliated_church,\n");
        query.append("          deleted\n");
        query.append("      )\n");
        query.append("      VALUES\n");
        query.append("      (\n");
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
                        ps.setString(1, sponsor.getSponsorSurname());
                        ps.setString(2, sponsor.getSponsorGivenName());
                        ps.setString(3, sponsor.getSponsorAffiliatedChurch());
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
        query.append("      sponsor_id,\n");
        query.append("      surname,\n");
        query.append("      given_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Person_Sponsor\n");
        query.append("  WHERE\n");
        query.append("      deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      surname,\n");
        query.append("      given_name\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{}, (rs, rowNum) -> {
                Sponsor row = new Sponsor();
                row.setSponsorId(rs.getInt("sponsor_id"));
                row.setSponsorSurname(rs.getString("surname"));
                row.setSponsorGivenName(rs.getString("given_name"));
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
        query.append(getSponsorList_SSP_RootQuery(serverSidePaginationRequest));
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
                Sponsor row = new Sponsor();
                row.setSponsorId(rs.getInt("sponsor_id"));
                row.setSponsorSurname(rs.getString("surname"));
                row.setSponsorGivenName(rs.getString("given_name"));
                row.setSponsorAffiliatedChurch(rs.getString("affiliated_church"));
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

    private String getSponsorList_SSP_RootQuery(ServerSidePaginationRequest<Sponsor> serverSidePaginationRequest) {
        //noinspection StringBufferReplaceableByString
        StringBuilder query = new StringBuilder();
        query.append("              SELECT\n");
        query.append("                  sponsor_id,\n");
        query.append("                  surname,\n");
        query.append("                  given_name,\n");
        query.append("                  affiliated_church,\n");
        query.append("                  (\n");
        query.append("                      SELECT\n");
        query.append("                          COUNT(*)\n");
        query.append("                      FROM\n");
        query.append("                          CRC.Rel_Student_Sponsor\n");
        query.append("                      WHERE\n");
        query.append("                          Rel_Student_Sponsor.sponsor_id = Person_Sponsor.sponsor_id\n");
        query.append("                          AND deleted = 0\n");
        query.append("                  ) as student_count\n");
        query.append("              FROM\n");
        query.append("                  CRC.Person_Sponsor\n");
        query.append("              WHERE\n");
        query.append("              (\n");
        query.append("                  deleted = 0\n");
        query.append("                  AND\n");
        query.append(getSponsorList_SSP_AdditionalWhereClause(serverSidePaginationRequest));
        query.append("              )\n");
        return query.toString();
    }

    private String getSponsorList_SSP_AdditionalWhereClause(ServerSidePaginationRequest<Sponsor> serverSidePaginationRequest) {
        StringBuilder whereClause = new StringBuilder();
        String nameFilter = serverSidePaginationRequest.getNameFilter() != null ? serverSidePaginationRequest.getNameFilter() : "";

        // NAME FILTER CLAUSE
        if (!"".equalsIgnoreCase(nameFilter)) {
            whereClause.append("                  (\n");
            whereClause.append("                    UPPER(Person_Sponsor.surname) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                    OR\n");
            whereClause.append("                    UPPER(Person_Sponsor.given_name) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                  )\n");
        } else {
            whereClause.append("                  (1=1)\n");
        }

        return whereClause.toString();
    }

    public Sponsor getSponsorDetail(int sponsorId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      sponsor_id,\n");
        query.append("      surname,\n");
        query.append("      given_name,\n");
        query.append("      affiliated_church\n");
        query.append("  FROM\n");
        query.append("      CRC.Person_Sponsor\n");
        query.append("  WHERE\n");
        query.append("      sponsor_id = ?\n");
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

    public Sponsor updateSponsor(Sponsor sponsor) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Person_Sponsor\n");
        query.append("  SET\n");
        query.append("      surname = ?,\n");
        query.append("      given_name = ?,\n");
        query.append("      affiliated_church = ?\n");
        query.append("  WHERE\n");
        query.append("      sponsor_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, sponsor.getSponsorSurname());
                        ps.setString(2, sponsor.getSponsorGivenName());
                        ps.setString(3, sponsor.getSponsorAffiliatedChurch());
                        ps.setInt(4, sponsor.getSponsorId());
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
        query.append("      CRC.Person_Sponsor\n");
        query.append("  SET\n");
        query.append("      deleted = 1\n");
        query.append("  WHERE\n");
        query.append("      sponsor_id = ?\n");
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

    // FILTERED LISTS

    public List<Sponsor> getSponsorListByStudentId(Integer studentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT DISTINCT\n");
        query.append("      Person_Sponsor.sponsor_id,\n");
        query.append("      Person_Sponsor.surname,\n");
        query.append("      Person_Sponsor.given_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Rel_Student_Sponsor\n");
        query.append("      LEFT JOIN CRC.Person_Sponsor ON Person_Sponsor.sponsor_id = Rel_Student_Sponsor.sponsor_id AND Person_Sponsor.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      Rel_Student_Sponsor.student_id = ?\n");
        query.append("      AND Rel_Student_Sponsor.deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      Person_Sponsor.given_name,\n");
        query.append("      Person_Sponsor.surname\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{studentId}, (rs, rowNum) -> {
                Sponsor row = new Sponsor();
                row.setSponsorId(rs.getInt("sponsor_id"));
                row.setSponsorSurname(rs.getString("surname"));
                row.setSponsorGivenName(rs.getString("given_name"));
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

    // JOINED QUERIES

    public Sponsor getCurrentSponsorDetailByStudentId(int studentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Person_Sponsor.sponsor_id,\n");
        query.append("      Person_Sponsor.surname,\n");
        query.append("      Person_Sponsor.given_name,\n");
        query.append("      Rel_Student_Sponsor.student_sponsor_id,\n");
        query.append("      Rel_Student_Sponsor.start_date\n");
        query.append("  FROM\n");
        query.append("      CRC.Rel_Student_Sponsor\n");
        query.append("      LEFT JOIN CRC.Person_Sponsor ON Person_Sponsor.sponsor_id = Rel_Student_Sponsor.sponsor_id AND Person_Sponsor.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      student_id = ?\n");
        query.append("      AND Rel_Student_Sponsor.deleted = 0\n");
        query.append("  ORDER BY\n");
        query.append("      start_date DESC,\n");
        query.append("      Rel_Student_Sponsor.updated_on DESC\n");
        query.append("  LIMIT 0, 1\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{studentId}, (rs, rowNum) -> {
                Sponsor row = new Sponsor();
                row.setSponsorId(rs.getInt("sponsor_id"));
                row.setSponsorSurname(rs.getString("surname"));
                row.setSponsorGivenName(rs.getString("given_name"));
                row.setRelationshipId(rs.getInt("student_sponsor_id"));
                row.setRelationshipStartDate(rs.getString("start_date"));
                return row;
            });
        } catch (EmptyResultDataAccessException e) {
            return new Sponsor();
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

}
