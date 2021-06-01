package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Loan;
import com.timsanalytics.crc.main.dao.finance.LoanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {
    private final LoanDao loanDao;

    @Autowired
    public LoanService(LoanDao loanDao) {
        this.loanDao = loanDao;
    }

    public Loan createLoan(Loan loan) {
        return this.loanDao.createLoan(loan);
    }

    public ServerSidePaginationResponse<Loan> getLoanList_SSP(ServerSidePaginationRequest<Loan> serverSidePaginationRequest) {
        ServerSidePaginationResponse<Loan> serverSidePaginationResponse = new ServerSidePaginationResponse<>();
        serverSidePaginationResponse.setServerSidePaginationRequest(serverSidePaginationRequest);
        List<Loan> loanList = this.loanDao.getLoanList_SSP(serverSidePaginationRequest);
        serverSidePaginationResponse.setData(loanList);
        serverSidePaginationResponse.setLoadedRecords(loanList.size());
        serverSidePaginationResponse.setTotalRecords(this.loanDao.getLoanList_SSP_TotalRecords(serverSidePaginationRequest));
        return serverSidePaginationResponse;
    }

    public Loan getLoanDetail(int loanId) {
        return this.loanDao.getLoanDetail(loanId);
    }

    public Loan updateLoan(Loan loan) {
        return this.loanDao.updateLoan(loan);
    }

    public KeyValue deleteLoan(String loanId) {
        return this.loanDao.deleteLoan(loanId);
    }
}
