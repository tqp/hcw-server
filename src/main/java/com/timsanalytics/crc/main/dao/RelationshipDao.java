package com.timsanalytics.crc.main.dao;

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

    public Relationship createRelationship(Relationship relationship) {
        this.printObjectService.PrintObject("createRelationship -> relationship", relationship);
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.StudentRelationship\n");
        query.append("      (\n");
        query.append("          student_id,\n");
        query.append("          person_id,\n");
        query.append("          relationship_type_id,\n");
        query.append("          blood_relative,\n");
        query.append("          deleted\n");
        query.append("      )\n");
        query.append("      VALUES\n");
        query.append("      (\n");
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
                        ps.setInt(1, relationship.getStudentId());
                        ps.setInt(2, relationship.getPersonId());
                        ps.setInt(3, relationship.getRelationshipTypeId());
                        ps.setInt(4, relationship.getRelationshipBloodRelative());
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

    public List<Relationship> getRelationshipListByStudentId(Integer studentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Relation.id,\n");
        query.append("      Relation.last_name,\n");
        query.append("      Relation.first_name,\n");
        query.append("      RelationshipType.name,\n");
        query.append("      StudentRelationship.blood_relative\n");
        query.append("  FROM\n");
        query.append("      CRC.Person Relation\n");
        query.append("      LEFT JOIN CRC.StudentRelationship ON StudentRelationship.person_id = Relation.id\n");
        query.append("      LEFT JOIN CRC.RelationshipType ON RelationshipType.id = StudentRelationship.relationship_type_id\n");
        query.append("  WHERE\n");
        query.append("      Relation.deleted = 0\n");
        query.append("      AND StudentRelationship.student_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{studentId}, (rs, rowNum) -> {
                Relationship row = new Relationship();
                row.setPersonId(rs.getInt("id"));
                row.setPersonSurname(rs.getString("last_name"));
                row.setPersonGivenName(rs.getString("first_name"));
                row.setRelationshipTypeName(rs.getString("name"));
                row.setRelationshipBloodRelative(rs.getInt("blood_relative"));
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

    public List<Relationship> getRelationshipListByRelationId(Integer relationId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Student.id,\n");
        query.append("      Student.last_name,\n");
        query.append("      Student.first_name,\n");
        query.append("      RelationshipType.name,\n");
        query.append("      StudentRelationship.blood_relative\n");
        query.append("  FROM\n");
        query.append("      CRC.Person Student\n");
        query.append("      LEFT JOIN CRC.StudentRelationship ON StudentRelationship.student_id = Student.id\n");
        query.append("      LEFT JOIN CRC.RelationshipType ON RelationshipType.id = StudentRelationship.relationship_type_id\n");
        query.append("  WHERE\n");
        query.append("      Student.deleted = 0\n");
        query.append("      AND StudentRelationship.person_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{relationId}, (rs, rowNum) -> {
                Relationship row = new Relationship();
                row.setPersonId(rs.getInt("id"));
                row.setPersonSurname(rs.getString("last_name"));
                row.setPersonGivenName(rs.getString("first_name"));
                row.setRelationshipTypeName(rs.getString("name"));
                row.setRelationshipBloodRelative(rs.getInt("blood_relative"));
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

    // OTHER

    public Relationship createRelationshipPerson(Relationship relationship) {
        this.printObjectService.PrintObject("createRelationshipPerson -> relationship", relationship);
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
        query.append("          4,\n");
        query.append("          0\n");
        query.append("      )\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, relationship.getPersonSurname());
                        ps.setString(2, relationship.getPersonGivenName());
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

}
