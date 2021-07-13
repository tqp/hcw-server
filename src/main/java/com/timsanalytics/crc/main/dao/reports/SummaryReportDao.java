package com.timsanalytics.crc.main.dao.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SummaryReportDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;

    @Autowired
    public SummaryReportDao(JdbcTemplate mySqlAuthJdbcTemplate) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
    }


    public Integer getStudentCountTotal() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      COUNT(*)\n");
        query.append("  FROM\n");
        query.append("  (\n");

        query.append("      SELECT\n");
        query.append("          Person_Student.student_id,\n");
        query.append("          Person_Student.surname,\n");
        query.append("          Person_Student.given_name,\n");
        query.append("          Rel_Student_Program_Status.start_date\n");
        query.append("      FROM\n");
        query.append("          CRC.Person_Student\n");
        query.append("          LEFT JOIN CRC.Rel_Student_Program_Status ON Rel_Student_Program_Status.student_id = Person_Student.student_id AND Rel_Student_Program_Status.deleted = 0\n");
        query.append("          LEFT OUTER JOIN CRC.Rel_Student_Program_Status effectiveDateComparison ON\n");
        query.append("          (\n");
        query.append("              effectiveDateComparison.student_id = Person_Student.student_id\n");
        query.append("              AND\n");
        query.append("              (\n");
        query.append("                  Rel_Student_Program_Status.start_date < effectiveDateComparison.start_date\n");
        query.append("                  OR\n");
        query.append("                  (\n");
        query.append("                      Rel_Student_Program_Status.start_date = effectiveDateComparison.start_date\n");
        query.append("                      AND\n");
        query.append("                      Rel_Student_Program_Status.student_program_status_id > effectiveDateComparison.student_program_status_id\n");
        query.append("                  )\n");
        query.append("              )\n");
        query.append("          )\n");
        query.append("      WHERE\n");
        query.append("      (\n");
        query.append("          Person_Student.deleted = 0\n");
        query.append("          AND effectiveDateComparison.student_id IS NULL\n");
        query.append("          AND\n");
        query.append("          (\n");
        query.append("              Rel_Student_Program_Status.program_status_level_one_id =1\n");
        query.append("              OR\n");
        query.append("              Rel_Student_Program_Status.program_status_level_one_id IS NULL\n");
        query.append("          )\n");
        query.append("      )\n");

        query.append("  ) AS COUNT\n");
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

    public Integer getStudentCountReintegrated() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      COUNT(*)\n");
        query.append("  FROM\n");
        query.append("  (\n");

        query.append("      SELECT\n");
        query.append("          Person_Student.student_id\n");
        query.append("      FROM\n");
        query.append("          CRC.Person_Student\n");

        query.append("          -- Current Program Status\n");
        query.append("          LEFT JOIN CRC.Rel_Student_Program_Status ON Rel_Student_Program_Status.student_id = Person_Student.student_id AND Rel_Student_Program_Status.deleted = 0\n");
        query.append("          LEFT OUTER JOIN CRC.Rel_Student_Program_Status effectiveDateComparison ON\n");
        query.append("          (\n");
        query.append("              effectiveDateComparison.student_id = Person_Student.student_id\n");
        query.append("              AND\n");
        query.append("              (\n");
        query.append("                  Rel_Student_Program_Status.start_date < effectiveDateComparison.start_date\n");
        query.append("                  OR\n");
        query.append("                  (\n");
        query.append("                      Rel_Student_Program_Status.start_date = effectiveDateComparison.start_date\n");
        query.append("                      AND\n");
        query.append("                      Rel_Student_Program_Status.student_program_status_id > effectiveDateComparison.student_program_status_id\n");
        query.append("                  )\n");
        query.append("              )\n");
        query.append("          )\n");
        query.append("      WHERE\n");
        query.append("      (\n");
        query.append("          Person_Student.deleted = 0\n");
        query.append("          AND Rel_Student_Program_Status.program_status_level_one_id = 1\n");
        query.append("          AND effectiveDateComparison.student_id IS NULL\n");
        query.append("      )\n");

        query.append("  ) AS COUNT\n");
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

    public Integer getStudentCountReintegratedRunaway() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      COUNT(*)\n");
        query.append("  FROM\n");
        query.append("  (\n");

        query.append("      SELECT\n");
        query.append("          Person_Student.student_id\n");
        query.append("      FROM\n");
        query.append("          CRC.Person_Student\n");

        query.append("          -- Current Program Status\n");
        query.append("          LEFT JOIN CRC.Rel_Student_Program_Status ON Rel_Student_Program_Status.student_id = Person_Student.student_id AND Rel_Student_Program_Status.deleted = 0\n");
        query.append("          LEFT OUTER JOIN CRC.Rel_Student_Program_Status effectiveDateComparison ON\n");
        query.append("          (\n");
        query.append("              effectiveDateComparison.student_id = Person_Student.student_id\n");
        query.append("              AND\n");
        query.append("              (\n");
        query.append("                  Rel_Student_Program_Status.start_date < effectiveDateComparison.start_date\n");
        query.append("                  OR\n");
        query.append("                  (\n");
        query.append("                      Rel_Student_Program_Status.start_date = effectiveDateComparison.start_date\n");
        query.append("                      AND\n");
        query.append("                      Rel_Student_Program_Status.student_program_status_id > effectiveDateComparison.student_program_status_id\n");
        query.append("                  )\n");
        query.append("              )\n");
        query.append("          )\n");
        query.append("      WHERE\n");
        query.append("      (\n");
        query.append("          Person_Student.deleted = 0\n");
        query.append("          AND Rel_Student_Program_Status.program_status_level_one_id = 2\n");
        query.append("          AND Rel_Student_Program_Status.program_status_level_two_id = 12\n");
        query.append("          AND effectiveDateComparison.student_id IS NULL\n");
        query.append("      )\n");

        query.append("  ) AS COUNT\n");
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

    public Integer getStudentCountFamiliesIntact() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      COUNT(*)\n");
        query.append("  FROM\n");
        query.append("  (\n");

        query.append("      SELECT\n");
        query.append("          Person_Student.student_id\n");
        query.append("      FROM\n");
        query.append("          CRC.Person_Student\n");

        query.append("          -- Current Caregiver\n");
        query.append("          LEFT JOIN CRC.Rel_Student_Caregiver ON Rel_Student_Caregiver.student_id = Person_Student.student_id AND Rel_Student_Caregiver.deleted = 0\n");
        query.append("          LEFT OUTER JOIN CRC.Rel_Student_Caregiver effectiveDateComparison ON\n");
        query.append("          (\n");
        query.append("              effectiveDateComparison.student_id = Person_Student.student_id\n");
        query.append("              AND\n");
        query.append("              (\n");
        query.append("                  Rel_Student_Caregiver.start_date < effectiveDateComparison.start_date\n");
        query.append("                  OR\n");
        query.append("                  (\n");
        query.append("                      Rel_Student_Caregiver.start_date = effectiveDateComparison.start_date\n");
        query.append("                      AND\n");
        query.append("                      Rel_Student_Caregiver.student_caregiver_id > effectiveDateComparison.student_caregiver_id\n");
        query.append("                  )\n");
        query.append("              )\n");
        query.append("          )\n");
        query.append("      WHERE\n");
        query.append("      (\n");
        query.append("          Person_Student.deleted = 0\n");
        query.append("          AND Rel_Student_Caregiver.family_of_origin_type_id = 2\n");
        query.append("          AND effectiveDateComparison.student_id IS NULL\n");
        query.append("      )\n");

        query.append("  ) AS COUNT\n");
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

    public Integer getStudentCountFamiliesIntactEnrolled() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      COUNT(*)\n");
        query.append("  FROM\n");
        query.append("  (\n");

        query.append("      SELECT\n");
        query.append("          Person_Student.student_id,\n");
        query.append("          Person_Student.surname,\n");
        query.append("          Person_Student.given_name\n");
        query.append("      FROM\n");
        query.append("          CRC.Person_Student\n");

        query.append("          -- Current Caregiver\n");
        query.append("          LEFT JOIN CRC.Rel_Student_Caregiver ON Rel_Student_Caregiver.student_id = Person_Student.student_id AND Rel_Student_Caregiver.deleted = 0\n");
        query.append("          LEFT OUTER JOIN CRC.Rel_Student_Caregiver effectiveDateComparisonCaregiver ON\n");
        query.append("          (\n");
        query.append("              effectiveDateComparisonCaregiver.student_id = Person_Student.student_id\n");
        query.append("              AND\n");
        query.append("              (\n");
        query.append("                  Rel_Student_Caregiver.start_date < effectiveDateComparisonCaregiver.start_date\n");
        query.append("                  OR\n");
        query.append("                  (\n");
        query.append("                      Rel_Student_Caregiver.start_date = effectiveDateComparisonCaregiver.start_date\n");
        query.append("                      AND\n");
        query.append("                      Rel_Student_Caregiver.student_caregiver_id > effectiveDateComparisonCaregiver.student_caregiver_id\n");
        query.append("                  )\n");
        query.append("              )\n");
        query.append("          )\n");

        query.append("          -- Current Program Status\n");
        query.append("          LEFT JOIN CRC.Rel_Student_Program_Status ON Rel_Student_Program_Status.student_id = Person_Student.student_id AND Rel_Student_Program_Status.deleted = 0\n");
        query.append("          LEFT OUTER JOIN CRC.Rel_Student_Program_Status effectiveDateComparisonProgramStatus ON\n");
        query.append("          (\n");
        query.append("              effectiveDateComparisonProgramStatus.student_id = Person_Student.student_id\n");
        query.append("              AND\n");
        query.append("              (\n");
        query.append("                  Rel_Student_Program_Status.start_date < effectiveDateComparisonProgramStatus.start_date\n");
        query.append("                  OR\n");
        query.append("                  (\n");
        query.append("                      Rel_Student_Program_Status.start_date = effectiveDateComparisonProgramStatus.start_date\n");
        query.append("                      AND\n");
        query.append("                      Rel_Student_Program_Status.student_program_status_id > effectiveDateComparisonProgramStatus.student_program_status_id\n");
        query.append("                  )\n");
        query.append("              )\n");
        query.append("          )\n");
        query.append("      WHERE\n");
        query.append("      (\n");
        query.append("          Person_Student.deleted = 0\n");
        query.append("          AND effectiveDateComparisonCaregiver.student_id IS NULL\n");
        query.append("          AND effectiveDateComparisonProgramStatus.student_id IS NULL\n");
        query.append("          AND Rel_Student_Caregiver.family_of_origin_type_id = 2\n");
        query.append("          AND Rel_Student_Program_Status.program_status_level_one_id = 1\n");
        query.append("      )\n");

        query.append("  ) AS COUNT\n");
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
}

