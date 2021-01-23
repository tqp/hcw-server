package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.main.beans.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class PaymentDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;
    private final UtilsDao utilsDao;

    @Autowired
    public PaymentDao(JdbcTemplate mySqlAuthJdbcTemplate,
                   UtilsDao utilsDao) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.utilsDao = utilsDao;
    }

    public Payment createPayment(Payment payment) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.Microfinance_Payment\n");
        query.append("      (\n");
        query.append("          loan_id,\n");
        query.append("          date,\n");
        query.append("          amount,\n");
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
                        ps.setInt(1, payment.getLoanId());
                        ps.setString(2, payment.getPaymentDate());
                        ps.setDouble(3, payment.getPaymentAmount());
                        return ps;
                    }
            );
            int lastInsertId = this.utilsDao.getLastInsertId();
            this.logger.debug("New Payment ID: " + lastInsertId);
            return this.getPaymentDetail(lastInsertId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public List<Payment> getPaymentList_SSP(ServerSidePaginationRequest<Payment> serverSidePaginationRequest) {
        int pageStart = (serverSidePaginationRequest.getPageIndex()) * serverSidePaginationRequest.getPageSize();
        int pageSize = serverSidePaginationRequest.getPageSize();

        String defaultSortField = "payment_id";
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
        query.append(getPaymentList_SSP_RootQuery(serverSidePaginationRequest));
        query.append("          ) AS ROOT_QUERY\n");
        query.append("          -- END ROOT QUERY\n");

        query.append("          ORDER BY\n");
        query.append(sortColumn).append(" ").append(sortDirection.toUpperCase()).append(",\n");
        query.append("              payment_id\n");
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
                Payment row = new Payment();
                row.setPaymentId(rs.getInt("payment_id"));
                row.setLoanId(rs.getInt("loan_id"));
                row.setLoanDescription(rs.getString("description"));
                row.setPaymentDate(rs.getString("date"));
                row.setPaymentAmount(rs.getDouble("amount"));
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
            this.logger.error("pageStart=" + pageSize + ", pageSize=" + pageSize);
            return null;
        }
    }

    private String getPaymentList_SSP_RootQuery(ServerSidePaginationRequest<Payment> serverSidePaginationRequest) {
        //noinspection StringBufferReplaceableByString
        StringBuilder query = new StringBuilder();
        query.append("              SELECT\n");
        query.append("                  payment_id,\n");
        query.append("                  Microfinance_Loan.loan_id,\n");
        query.append("                  Microfinance_Loan.description,\n");
        query.append("                  Microfinance_Payment.amount,\n");
        query.append("                  date,\n");
        query.append("                  Person_Caregiver.caregiver_id,\n");
        query.append("                  Person_Caregiver.surname,\n");
        query.append("                  Person_Caregiver.given_name\n");
        query.append("              FROM\n");
        query.append("                  CRC.Microfinance_Payment\n");
        query.append("                  LEFT JOIN CRC.Microfinance_Loan ON Microfinance_Loan.loan_id = Microfinance_Payment.loan_id AND Microfinance_Loan.deleted = 0\n");
        query.append("                  LEFT JOIN CRC.Person_Caregiver ON Person_Caregiver.caregiver_id = Microfinance_Loan.caregiver_id AND Person_Caregiver.deleted = 0\n");
        query.append("              WHERE\n");
        query.append("              (\n");
        query.append("                  Microfinance_Payment.deleted = 0\n");
        query.append("                  AND\n");
        query.append(getPaymentList_SSP_AdditionalWhereClause(serverSidePaginationRequest));
        query.append("              )\n");
        return query.toString();
    }

    private String getPaymentList_SSP_AdditionalWhereClause(ServerSidePaginationRequest<Payment> serverSidePaginationRequest) {
        StringBuilder whereClause = new StringBuilder();
        String nameFilter = serverSidePaginationRequest.getNameFilter() != null ? serverSidePaginationRequest.getNameFilter() : "";

        // NAME FILTER CLAUSE
        if (!"".equalsIgnoreCase(nameFilter)) {
            whereClause.append("                  (\n");
            whereClause.append("                    UPPER(Person_Caregiver.surname) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                    OR\n");
            whereClause.append("                    UPPER(Person_Caregiver.given_name) LIKE UPPER('%").append(nameFilter).append("%')\n");
            whereClause.append("                  )\n");
        } else {
            whereClause.append("                  (1=1)\n");
        }

        return whereClause.toString();
    }

    public int getPaymentList_SSP_TotalRecords(ServerSidePaginationRequest<Payment> serverSidePaginationRequest) {
        StringBuilder query = new StringBuilder();
        query.append("          SELECT\n");
        query.append("              COUNT(*)\n");
        query.append("          FROM\n");
        query.append("          -- ROOT QUERY\n");
        query.append("          (\n");
        query.append(getPaymentList_SSP_RootQuery(serverSidePaginationRequest));
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

    public Payment getPaymentDetail(Integer paymentId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      payment_id,\n");
        query.append("      Microfinance_Loan.loan_id,\n");
        query.append("      Microfinance_Loan.description,\n");
        query.append("      Microfinance_Payment.amount,\n");
        query.append("      date,\n");
        query.append("      Person_Caregiver.caregiver_id,\n");
        query.append("      Person_Caregiver.surname,\n");
        query.append("      Person_Caregiver.given_name\n");
        query.append("  FROM\n");
        query.append("      CRC.Microfinance_Payment\n");
        query.append("      LEFT JOIN CRC.Microfinance_Loan ON Microfinance_Loan.loan_id = Microfinance_Payment.loan_id AND Microfinance_Loan.deleted = 0\n");
        query.append("      LEFT JOIN CRC.Person_Caregiver ON Person_Caregiver.caregiver_id = Microfinance_Loan.caregiver_id AND Person_Caregiver.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      payment_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{paymentId}, (rs, rowNum) -> {
                Payment row = new Payment();
                row.setPaymentId(rs.getInt("payment_id"));
                row.setLoanId(rs.getInt("loan_id"));
                row.setLoanDescription(rs.getString("description"));
                row.setPaymentAmount(rs.getDouble("amount"));
                row.setPaymentDate(rs.getString("date"));
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

    public Payment updatePayment(Payment payment) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Microfinance_Payment\n");
        query.append("  SET\n");
        query.append("      amount = ?,\n");
        query.append("      date = ?\n");
        query.append("  WHERE\n");
        query.append("      payment_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setDouble(1, payment.getPaymentAmount());
                        ps.setString(2, payment.getPaymentDate());
                        ps.setInt(3, payment.getPaymentId());
                        return ps;
                    }
            );
            return this.getPaymentDetail(payment.getPaymentId());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public KeyValue deletePayment(String paymentGuid) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Microfinance_Payment\n");
        query.append("  SET\n");
        query.append("      deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        this.logger.trace("PERSON_GUID=" + paymentGuid);
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, paymentGuid);
                        return ps;
                    }
            );
            return new KeyValue("paymentGuid", paymentGuid);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }
}
