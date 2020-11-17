package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.main.beans.Loan;
import com.timsanalytics.crc.main.beans.Payment;
import com.timsanalytics.crc.main.beans.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class FinanceDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;
    private final UtilsDao utilsDao;

    @Autowired
    public FinanceDao(JdbcTemplate mySqlAuthJdbcTemplate,
                      UtilsDao utilsDao) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.utilsDao = utilsDao;
    }

    // Header Boxes

    public Double getTotalCommitted() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      SUM(amount)\n");
        query.append("  FROM\n");
        query.append("      CRC.Microfinance_Loan\n");
        query.append("  WHERE\n");
        query.append("      deleted = 0\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            Double amount = this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{}, Double.class);
            return amount == null ? 0 : amount;
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return 0.0;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return 0.0;
        }
    }

    public Double getTotalPaid() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      SUM(amount)\n");
        query.append("  FROM\n");
        query.append("      CRC.Microfinance_Payment\n");
        query.append("  WHERE\n");
        query.append("      deleted = 0\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            Double amount = this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{}, Double.class);
            return amount == null ? 0 : amount;
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return 0.0;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return 0.0;
        }
    }

    public List<Loan> getLoanListByCaregiverId(Integer caregiverId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Microfinance_Loan.loan_id,\n");
        query.append("      caregiver_id,\n");
        query.append("      description,\n");
        query.append("      Microfinance_Loan.amount AS loan_amount,\n");
        query.append("      SUM(Microfinance_Payment.amount) AS payment_total\n");
        query.append("  FROM\n");
        query.append("      CRC.Microfinance_Loan\n");
        query.append("      LEFT JOIN CRC.Microfinance_Payment ON Microfinance_Payment.loan_id = Microfinance_Loan.loan_id AND Microfinance_Payment.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      Microfinance_Loan.caregiver_id = ?\n");
        query.append("      AND Microfinance_Loan.deleted = 0\n");
        query.append("  GROUP BY\n");
        query.append("      Microfinance_Loan.loan_id,\n");
        query.append("      caregiver_id,\n");
        query.append("      description,\n");
        query.append("      Microfinance_Loan.amount\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.query(query.toString(), new Object[]{caregiverId}, (rs, rowNum) -> {
                Loan row = new Loan();
                row.setLoanId(rs.getInt("loan_id"));
                row.setCaregiverId(rs.getInt("caregiver_id"));
                row.setLoanDescription(rs.getString("description"));
                row.setLoanAmount(rs.getDouble("loan_amount"));
                row.setAmountPaid(rs.getDouble("payment_total"));
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

    // PAYMENTS


}
