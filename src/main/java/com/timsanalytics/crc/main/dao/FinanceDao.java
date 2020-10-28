package com.timsanalytics.crc.main.dao;

import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.main.beans.FinanceLoan;
import com.timsanalytics.crc.main.beans.FinancePayment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;

    @Autowired
    public FinanceDao(JdbcTemplate mySqlAuthJdbcTemplate) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
    }

    public List<FinanceLoan> getLoanList_SSP(ServerSidePaginationRequest<FinanceLoan> serverSidePaginationRequest) {
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
        query.append(getFinanceListByParticipant_SSP_RootQuery(serverSidePaginationRequest));
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
                FinanceLoan row = new FinanceLoan();
                row.setLoanId(rs.getInt("loan_id"));
                row.setCaregiverId(rs.getInt("caregiver_id"));
                row.setCaregiverSurname(rs.getString("surname"));
                row.setCaregiverGivenName(rs.getString("given_name"));
                row.setBusinessDescription(rs.getString("business_description"));
                row.setLoanAmount(rs.getDouble("loan_amount"));
                row.setAmountPaid(rs.getDouble("amount_paid"));
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

    private String getFinanceListByParticipant_SSP_RootQuery(ServerSidePaginationRequest<FinanceLoan> serverSidePaginationRequest) {
        //noinspection StringBufferReplaceableByString
        StringBuilder query = new StringBuilder();
        query.append("              SELECT\n");
        query.append("                  loan_id,\n");
        query.append("                  Person_Caregiver.caregiver_id,\n");
        query.append("                  Person_Caregiver.surname,\n");
        query.append("                  Person_Caregiver.given_name,\n");
        query.append("                  business_description,\n");
        query.append("                  loan_amount,\n");
        query.append("                  (\n");
        query.append("                      SELECT\n");
        query.append("                          SUM(payment_amount) AS amount_paid\n");
        query.append("                      FROM\n");
        query.append("                          CRC.Microfinance_Payment\n");
        query.append("                      WHERE\n");
        query.append("                          loan_id = Microfinance_Loan.loan_id\n");
        query.append("                          AND deleted = 0\n");
        query.append("                  ) AS amount_paid\n");
        query.append("              FROM\n");
        query.append("                  CRC.Microfinance_Loan\n");
        query.append("                  LEFT JOIN CRC.Person_Caregiver ON Person_Caregiver.caregiver_id = Microfinance_Loan.caregiver_id AND Person_Caregiver.deleted = 0\n");
        query.append("              WHERE\n");
        query.append("              (\n");
        query.append("                  Microfinance_Loan.deleted = 0\n");
        query.append("                  AND\n");
        query.append("                  ");
        query.append(getFinanceListByParticipant_SSP_AdditionalWhereClause(serverSidePaginationRequest));
        query.append("              )");
        ;
        return query.toString();
    }

    private String getFinanceListByParticipant_SSP_AdditionalWhereClause(ServerSidePaginationRequest<FinanceLoan> serverSidePaginationRequest) {
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

    public int getLoanList_SSP_TotalRecords(ServerSidePaginationRequest<FinanceLoan> serverSidePaginationRequest) {
        StringBuilder query = new StringBuilder();
        query.append("          SELECT\n");
        query.append("              COUNT(*)\n");
        query.append("          FROM\n");
        query.append("          -- ROOT QUERY\n");
        query.append("          (\n");
        query.append(getFinanceListByParticipant_SSP_RootQuery(serverSidePaginationRequest));
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

    public List<FinancePayment> getPaymentList_SSP(ServerSidePaginationRequest<FinancePayment> serverSidePaginationRequest) {
        int pageStart = (serverSidePaginationRequest.getPageIndex()) * serverSidePaginationRequest.getPageSize();
        int pageSize = serverSidePaginationRequest.getPageSize();

        String defaultSortField = "loan_id";
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
        query.append("              loan_id\n");
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
                FinancePayment row = new FinancePayment();
                row.setPaymentId(rs.getInt("payment_id"));
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

    private String getPaymentList_SSP_RootQuery(ServerSidePaginationRequest<FinancePayment> serverSidePaginationRequest) {
        //noinspection StringBufferReplaceableByString
        StringBuilder query = new StringBuilder();
        query.append("              SELECT\n");
        query.append("                  payment_id,\n");
        query.append("                  loan_id,\n");
        query.append("                  payment_amount\n");
        query.append("              FROM\n");
        query.append("                  CRC.Microfinance_Payment\n");
        query.append("              WHERE\n");
        query.append("              (\n");
        query.append("                  deleted = 0\n");
        query.append("                  AND\n");
        query.append("                  ");
        query.append(getPaymentList_SSP_AdditionalWhereClause(serverSidePaginationRequest));
        query.append("              )");
        ;
        return query.toString();
    }

    private String getPaymentList_SSP_AdditionalWhereClause(ServerSidePaginationRequest<FinancePayment> serverSidePaginationRequest) {
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

    public int getPaymentList_SSP_TotalRecords(ServerSidePaginationRequest<FinancePayment> serverSidePaginationRequest) {
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

    // Header Boxes

    public Double getTotalCommitted() {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      SUM(loan_amount)\n");
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
        query.append("      SUM(payment_amount)\n");
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
}
