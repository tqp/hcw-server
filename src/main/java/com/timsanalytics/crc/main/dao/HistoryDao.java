package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.main.beans.History;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;
    private final UtilsDao utilsDao;

    @Autowired
    public HistoryDao(JdbcTemplate mySqlAuthJdbcTemplate, UtilsDao utilsDao) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.utilsDao = utilsDao;
    }

    public List<History> getHistoryListByStudentId(Integer studentId) {
        StringBuilder query = new StringBuilder();
        query.append("  (\n");
        query.append("      SELECT\n");
        query.append("          1 AS history_type_id,\n");
        query.append("          'Program Status' AS history_type_name,\n");
        query.append("          Rel_Student_Program_Status.deleted AS deleted_status,\n");
        query.append("          start_date,\n");
        query.append("          Rel_Student_Program_Status.student_program_status_id AS relationship_id,\n");
        query.append("          student_program_status_id AS entity_id,\n");
        query.append("          Ref_Program_Status_Type.name AS history_description\n");
        query.append("      FROM\n");
        query.append("          CRC.Rel_Student_Program_Status\n");
        query.append("          LEFT JOIN CRC.Ref_Program_Status_Type ON Ref_Program_Status_Type.program_status_type_id = program_status_level_one_id AND Ref_Program_Status_Type.deleted = 0\n");
        query.append("      WHERE\n");
        query.append("          student_id = ?\n");
        query.append("          AND Rel_Student_Program_Status.deleted = 0\n");
        query.append("  )\n");
        query.append("  UNION\n");
        query.append("  (\n");
        query.append("      SELECT\n");
        query.append("          2 AS history_type_id,\n");
        query.append("          'Caregiver' AS history_type_name,\n");
        query.append("          Rel_Student_Caregiver.deleted,\n");
        query.append("          start_date,\n");
        query.append("          Rel_Student_Caregiver.student_caregiver_id AS relationship_id,\n");
        query.append("          Person_Caregiver.caregiver_id AS entity_id,\n");
        query.append("          CONCAT(Person_Caregiver.given_name, \" \", Person_Caregiver.surname) AS history_description\n");
        query.append("      FROM\n");
        query.append("          CRC.Rel_Student_Caregiver\n");
        query.append("          LEFT JOIN CRC.Person_Caregiver ON Person_Caregiver.caregiver_id = Rel_Student_Caregiver.caregiver_id AND Person_Caregiver.deleted = 0\n");
        query.append("      WHERE\n");
        query.append("          student_id = ?\n");
        query.append("          AND Rel_Student_Caregiver.deleted = 0\n");
        query.append("  )\n");
        query.append("  UNION\n");
        query.append("  (\n");
        query.append("      SELECT\n");
        query.append("          3 AS history_type_id,\n");
        query.append("          'Case Manager' AS history_type_name,\n");
        query.append("          Rel_Student_Case_Manager.deleted,\n");
        query.append("          start_date,\n");
        query.append("          Rel_Student_Case_Manager.case_manager_user_id AS relationship_id,\n");
        query.append("          Person_Case_Manager.case_manager_user_id AS entity_id,\n");
        query.append("          CONCAT(Person_Case_Manager.given_name, \" \", Person_Case_Manager.surname)\n");
        query.append("      FROM\n");
        query.append("          CRC.Rel_Student_Case_Manager\n");
        query.append("          LEFT JOIN CRC.Person_Case_Manager ON Person_Case_Manager.case_manager_user_id = Rel_Student_Case_Manager.case_manager_user_id AND Person_Case_Manager.deleted = 0\n");
        query.append("      WHERE\n");
        query.append("          student_id = ?\n");
        query.append("          AND Rel_Student_Case_Manager.deleted = 0\n");
        query.append("  )\n");
        query.append("  UNION\n");
        query.append("  (\n");
        query.append("      SELECT\n");
        query.append("          4 AS history_type_id,\n");
        query.append("          'Sponsor' AS history_type_name,\n");
        query.append("          Rel_Student_Sponsor.deleted,\n");
        query.append("          start_date,\n");
        query.append("          Rel_Student_Sponsor.student_sponsor_id AS relationship_id,\n");
        query.append("          Person_Sponsor.sponsor_id AS entity_id,\n");
        query.append("          CONCAT(Person_Sponsor.given_name, \" \", Person_Sponsor.surname)\n");
        query.append("      FROM\n");
        query.append("          CRC.Rel_Student_Sponsor\n");
        query.append("          LEFT JOIN CRC.Person_Sponsor ON Person_Sponsor.sponsor_id = Rel_Student_Sponsor.sponsor_id AND Person_Sponsor.deleted = 0\n");
        query.append("      WHERE\n");
        query.append("          student_id = ?\n");
        query.append("          AND Rel_Student_Sponsor.deleted = 0\n");
        query.append("  )\n");
        query.append("  ORDER BY\n");
        query.append("      start_date DESC,\n");
        query.append("      history_type_name ASC\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{studentId, studentId, studentId, studentId}, (rs, rowNum) -> {
                History row = new History();
                row.setEntityTypeId(rs.getInt("history_type_id"));
                row.setEntityTypeName(rs.getString("history_type_name"));
                row.setDeletedStatus(rs.getInt("deleted_status"));
                row.setStartDate(rs.getString("start_date"));
                row.setRelationshipId(rs.getInt("relationship_id"));
                row.setEntityId(rs.getInt("entity_id"));
                row.setEntityDescription(rs.getString("history_description"));
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
