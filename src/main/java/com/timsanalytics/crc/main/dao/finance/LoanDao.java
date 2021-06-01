package com.timsanalytics.crc.main.dao.finance;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.main.beans.Loan;
import com.timsanalytics.crc.main.beans.Payment;
import com.timsanalytics.crc.main.beans.Loan;
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
public class LoanDao {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final JdbcTemplate mySqlAuthJdbcTemplate;
    private final UtilsDao utilsDao;

    @Autowired
    public LoanDao(JdbcTemplate mySqlAuthJdbcTemplate,
                      UtilsDao utilsDao) {
        this.mySqlAuthJdbcTemplate = mySqlAuthJdbcTemplate;
        this.utilsDao = utilsDao;
    }

    public Loan createLoan(Loan loan) {
        StringBuilder query = new StringBuilder();
        query.append("  INSERT INTO\n");
        query.append("      CRC.Microfinance_Loan\n");
        query.append("      (\n");
        query.append("          caregiver_id,\n");
        query.append("          amount,\n");
        query.append("          description,\n");
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
                        ps.setInt(1, loan.getCaregiverId());
                        ps.setDouble(2, loan.getLoanAmount());
                        ps.setString(3, loan.getLoanDescription());
                        return ps;
                    }
            );
            int lastInsertId = this.utilsDao.getLastInsertId();
            this.logger.debug("New Loan ID: " + lastInsertId);
            return this.getLoanDetail(lastInsertId);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public List<Loan> getLoanList_SSP(ServerSidePaginationRequest<Loan> serverSidePaginationRequest) {
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
        query.append(getLoanListByParticipant_SSP_RootQuery(serverSidePaginationRequest));
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
                Loan row = new Loan();
                row.setLoanId(rs.getInt("loan_id"));
                row.setCaregiverId(rs.getInt("caregiver_id"));
                row.setCaregiverSurname(rs.getString("surname"));
                row.setCaregiverGivenName(rs.getString("given_name"));
                row.setLoanDescription(rs.getString("description"));
                row.setLoanAmount(rs.getDouble("amount"));
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

    private String getLoanListByParticipant_SSP_RootQuery(ServerSidePaginationRequest<Loan> serverSidePaginationRequest) {
        //noinspection StringBufferReplaceableByString
        StringBuilder query = new StringBuilder();
        query.append("              SELECT\n");
        query.append("                  loan_id,\n");
        query.append("                  Person_Caregiver.caregiver_id,\n");
        query.append("                  Person_Caregiver.surname,\n");
        query.append("                  Person_Caregiver.given_name,\n");
        query.append("                  description,\n");
        query.append("                  amount,\n");
        query.append("                  (\n");
        query.append("                      SELECT\n");
        query.append("                          SUM(amount) AS amount_paid\n");
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
        query.append(getLoanListByParticipant_SSP_AdditionalWhereClause(serverSidePaginationRequest));
        query.append("              )\n");
        return query.toString();
    }

    private String getLoanListByParticipant_SSP_AdditionalWhereClause(ServerSidePaginationRequest<Loan> serverSidePaginationRequest) {
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

    public int getLoanList_SSP_TotalRecords(ServerSidePaginationRequest<Loan> serverSidePaginationRequest) {
        StringBuilder query = new StringBuilder();
        query.append("          SELECT\n");
        query.append("              COUNT(*)\n");
        query.append("          FROM\n");
        query.append("          -- ROOT QUERY\n");
        query.append("          (\n");
        query.append(getLoanListByParticipant_SSP_RootQuery(serverSidePaginationRequest));
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

    public Loan getLoanDetail(Integer loanId) {
        StringBuilder query = new StringBuilder();
        query.append("  SELECT\n");
        query.append("      Person_Caregiver.caregiver_id,\n");
        query.append("      Person_Caregiver.surname,\n");
        query.append("      Person_Caregiver.given_name,\n");
        query.append("      loan_id,\n");
        query.append("      amount,\n");
        query.append("      description\n");
        query.append("  FROM\n");
        query.append("      CRC.Microfinance_Loan\n");
        query.append("      LEFT JOIN CRC.Person_Caregiver ON Person_Caregiver.caregiver_id = Microfinance_Loan.caregiver_id AND Microfinance_Loan.deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      loan_id = ?\n");
        query.append("      AND Microfinance_Loan.deleted = 0\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            return this.mySqlAuthJdbcTemplate.queryForObject(query.toString(), new Object[]{loanId}, (rs, rowNum) -> {
                Loan row = new Loan();
                row.setCaregiverId(rs.getInt("caregiver_id"));
                row.setCaregiverSurname(rs.getString("surname"));
                row.setCaregiverGivenName(rs.getString("given_name"));
                row.setLoanId(rs.getInt("loan_id"));
                row.setLoanAmount(rs.getDouble("amount"));
                row.setLoanDescription(rs.getString("description"));
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

    public Loan updateLoan(Loan loan) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Microfinance_Loan\n");
        query.append("  SET\n");
        query.append("      amount = ?,\n");
        query.append("      description = ?\n");
        query.append("  WHERE\n");
        query.append("      loan_id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setDouble(1, loan.getLoanAmount());
                        ps.setString(2, loan.getLoanDescription());
                        ps.setInt(3, loan.getLoanId());
                        return ps;
                    }
            );
            return this.getLoanDetail(loan.getLoanId());
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }

    public KeyValue deleteLoan(String loanGuid) {
        StringBuilder query = new StringBuilder();
        query.append("  UPDATE\n");
        query.append("      CRC.Microfinance_Loan\n");
        query.append("  SET\n");
        query.append("      deleted = 0\n");
        query.append("  WHERE\n");
        query.append("      id = ?\n");
        this.logger.trace("SQL:\n" + query.toString());
        this.logger.trace("PERSON_GUID=" + loanGuid);
        try {
            this.mySqlAuthJdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(query.toString());
                        ps.setString(1, loanGuid);
                        return ps;
                    }
            );
            return new KeyValue("loanGuid", loanGuid);
        } catch (EmptyResultDataAccessException e) {
            this.logger.error("EmptyResultDataAccessException: " + e);
            return null;
        } catch (Exception e) {
            this.logger.error("Exception: " + e);
            return null;
        }
    }
}
