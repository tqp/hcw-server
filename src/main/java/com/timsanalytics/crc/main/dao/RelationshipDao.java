package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.KeyValueLong;
import com.timsanalytics.crc.main.beans.ProgramStatus;
import com.timsanalytics.crc.main.beans.Relationship;
import com.timsanalytics.crc.utils.PrintObjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class RelationshipDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;
    private final PrintObjectService printObjectService;

    @Autowired
    public RelationshipDao(JdbcTemplate mySqlAuthJdbcTemplate,
                           PrintObjectService printObjectService) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.printObjectService = printObjectService;
    }

    // CAREGIVER

    public Relationship createCaregiverRelationship(String username, Relationship relationship) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.Rel_Student_Caregiver\n");
        query.append("      (\n");
        query.append("          student_id,\n");
        query.append("          caregiver_id,\n");
        query.append("          start_date,\n");
        query.append("          tier_type_id,\n");
        query.append("          relationship_type_id,\n");
        query.append("          family_of_origin_type_id,\n");
        query.append("          created_on,\n");
        query.append("          created_by,\n");
        query.append("          updated_on,\n");
        query.append("          updated_by,\n");
        query.append("          deleted\n");
        query.append("      )\n");
        query.append("      VALUES\n");
        query.append("      (\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          now(),\n");
        query.append("          ?,\n");
        query.append("          now(),\n");
        query.append("          ?,\n");
        query.append("          0\n");
        query.append("      )\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, relationship.getStudentId());
                        ps.setInt(2, relationship.getRelationshipEntityId());
                        ps.setString(3, relationship.getRelationshipStartDate());
                        ps.setInt(4, relationship.getRelationshipTierTypeId());
                        ps.setInt(5, relationship.getRelationshipTypeId());
                        ps.setInt(6, relationship.getRelationshipFamilyOfOriginTypeId());
                        ps.setString(7, username);
                        ps.setString(8, username);
                        return ps;
                    }
            );
            return relationship;
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public List<Relationship> getStudentListByCaregiverId(Integer caregiverId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Person_Student.student_id,\n");
        query.append("      Person_Student.surname,\n");
        query.append("      Person_Student.given_name,\n");
        query.append("      Rel_Student_Caregiver.start_date,\n");
        query.append("      Ref_Tier_Type.tier_type_id AS tier_type_id,\n");
        query.append("      Ref_Tier_Type.name AS tier_type_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Rel_Student_Caregiver\n");
        query.append("      LEFT JOIN CRC.Person_Student ON Person_Student.student_id = Rel_Student_Caregiver.student_id AND Person_Student.deleted = 0\n");
        query.append("      LEFT JOIN CRC.Ref_Tier_Type ON Ref_Tier_Type.tier_type_id = Rel_Student_Caregiver.tier_type_id AND Ref_Tier_Type.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      Rel_Student_Caregiver.caregiver_id = ?\n");
        query.append("      AND Rel_Student_Caregiver.deleted = 0\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{caregiverId}, (rs, rowNum) -> {
                Relationship row = new Relationship();
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("surname"));
                row.setStudentGivenName(rs.getString("given_name"));
                row.setRelationshipStartDate(rs.getString("start_date"));
                row.setRelationshipTierTypeId(rs.getInt("tier_type_id"));
                row.setRelationshipTierTypeName(rs.getString("tier_type_name"));
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

    public Relationship updateCaregiverRelationship(String username, Relationship relationship) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Rel_Student_Caregiver\n");
        query.append("  SET\n");
        query.append("      student_id = ?,\n");
        query.append("      caregiver_id = ?,\n");
        query.append("      start_date = ?,\n");
        query.append("      tier_type_id = ?,\n");
        query.append("      relationship_type_id = ?,\n");
        query.append("      family_of_origin_type_id = ?,\n");
        query.append("      updated_on = now(),\n");
        query.append("      updated_by = ?\n");
        query.append("  WHERE\n");
        query.append("      student_caregiver_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, relationship.getStudentId());
                        ps.setInt(2, relationship.getRelationshipEntityId());
                        ps.setString(3, relationship.getRelationshipStartDate());
                        ps.setInt(4, relationship.getRelationshipTierTypeId());
                        ps.setInt(5, relationship.getRelationshipTypeId());
                        ps.setInt(6, relationship.getRelationshipFamilyOfOriginTypeId());
                        ps.setString(7, username);
                        ps.setInt(8, relationship.getRelationshipId());
                        return ps;
                    }
            );
            return relationship;
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public KeyValueLong deleteCaregiverRelationship(Integer relationshipId) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Rel_Student_Caregiver\n");
        query.append("  SET\n");
        query.append("      deleted = 1\n");
        query.append("  WHERE\n");
        query.append("      student_caregiver_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, relationshipId);
                        return ps;
                    }
            );
            return new KeyValueLong("relationshipId", relationshipId.longValue());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    // CASE MANAGER

    public Relationship createCaseManagerRelationship(String username, Relationship relationship) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.Rel_Student_Case_Manager\n");
        query.append("      (\n");
        query.append("          student_id,\n");
        query.append("          case_manager_id,\n");
        query.append("          start_date,\n");
        query.append("          created_on,\n");
        query.append("          created_by,\n");
        query.append("          updated_on,\n");
        query.append("          updated_by,\n");
        query.append("          deleted\n");
        query.append("      )\n");
        query.append("      VALUES\n");
        query.append("      (\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          now(),\n");
        query.append("          ?,\n");
        query.append("          now(),\n");
        query.append("          ?,\n");
        query.append("          0\n");
        query.append("      )\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, relationship.getStudentId());
                        ps.setInt(2, relationship.getRelationshipEntityId());
                        ps.setString(3, relationship.getRelationshipStartDate());
                        ps.setString(4, username);
                        ps.setString(5, username);
                        return ps;
                    }
            );
            return relationship;
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public List<Relationship> getStudentListByCaseManagerId(Integer caseManagerId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Person_Student.student_id,\n");
        query.append("      Person_Student.surname,\n");
        query.append("      Person_Student.given_name,\n");
        query.append("      Rel_Student_Case_Manager.start_date\n");
        query.append("  FROM\n");
        query.append("      CRC.Rel_Student_Case_Manager\n");
        query.append("      LEFT JOIN CRC.Person_Student ON Person_Student.student_id = Rel_Student_Case_Manager.student_id AND Person_Student.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      Rel_Student_Case_Manager.case_manager_id = ?\n");
        query.append("      AND Rel_Student_Case_Manager.deleted = 0\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{caseManagerId}, (rs, rowNum) -> {
                Relationship row = new Relationship();
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("surname"));
                row.setStudentGivenName(rs.getString("given_name"));
                row.setRelationshipStartDate(rs.getString("start_date"));
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

    // SPONSOR

    public Relationship createSponsorRelationship(String username, Relationship relationship) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.Rel_Student_Sponsor\n");
        query.append("      (\n");
        query.append("          student_id,\n");
        query.append("          sponsor_id,\n");
        query.append("          start_date,\n");
        query.append("          created_on,\n");
        query.append("          created_by,\n");
        query.append("          updated_on,\n");
        query.append("          updated_by,\n");
        query.append("          deleted\n");
        query.append("      )\n");
        query.append("      VALUES\n");
        query.append("      (\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          ?,\n");
        query.append("          now(),\n");
        query.append("          ?,\n");
        query.append("          now(),\n");
        query.append("          ?,\n");
        query.append("          0\n");
        query.append("      )\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setInt(1, relationship.getStudentId());
                        ps.setInt(2, relationship.getRelationshipEntityId());
                        ps.setString(3, relationship.getRelationshipStartDate());
                        ps.setString(4, username);
                        ps.setString(5, username);
                        return ps;
                    }
            );
            return relationship;
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public List<Relationship> getStudentListBySponsorId(Integer sponsorId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Person_Student.student_id,\n");
        query.append("      Person_Student.surname,\n");
        query.append("      Person_Student.given_name,\n");
        query.append("      Rel_Student_Sponsor.start_date\n");
        query.append("  FROM\n");
        query.append("      CRC.Rel_Student_Sponsor\n");
        query.append("      LEFT JOIN CRC.Person_Student ON Person_Student.student_id = Rel_Student_Sponsor.student_id AND Person_Student.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      Rel_Student_Sponsor.sponsor_id = ?\n");
        query.append("      AND Rel_Student_Sponsor.deleted = 0\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{sponsorId}, (rs, rowNum) -> {
                Relationship row = new Relationship();
                row.setStudentId(rs.getInt("student_id"));
                row.setStudentSurname(rs.getString("surname"));
                row.setStudentGivenName(rs.getString("given_name"));
                row.setRelationshipStartDate(rs.getString("start_date"));
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

    // PROGRAM STATUS

    public ProgramStatus createProgramStatusEntry(String username, ProgramStatus programStatus) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.Rel_Student_Program_Status\n");
        query.append("      (\n");
        query.append("          student_id,\n");
        query.append("          program_status_level_one_id,\n");
        query.append("          program_status_level_two_id,\n");
        query.append("          program_status_level_three_id,\n");
        query.append("          start_date,\n");
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
                        ps.setInt(1, programStatus.getStudentId());
                        ps.setInt(2, programStatus.getProgramStatusLevelOneId());
                        ps.setInt(3, programStatus.getProgramStatusLevelTwoId());
                        ps.setInt(4, programStatus.getProgramStatusLevelThreeId());
                        ps.setString(5, programStatus.getProgramStatusStartDate());
                        return ps;
                    }
            );
            return programStatus;
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

}
