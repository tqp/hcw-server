package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.FinanceLoan;
import com.timsanalytics.crc.main.beans.FinancePayment;
import com.timsanalytics.crc.main.beans.Person;
import com.timsanalytics.crc.main.dao.FinanceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceService {
    private final FinanceDao financeDao;

    @Autowired
    public FinanceService(FinanceDao financeDao) {
        this.financeDao = financeDao;
    }

    public ServerSidePaginationResponse<FinanceLoan> getLoanList_SSP(ServerSidePaginationRequest<FinanceLoan> serverSidePaginationRequest) {
        ServerSidePaginationResponse<FinanceLoan> serverSidePaginationResponse = new ServerSidePaginationResponse<>();
        serverSidePaginationResponse.setServerSidePaginationRequest(serverSidePaginationRequest);
        List<FinanceLoan> loanList = this.financeDao.getLoanList_SSP(serverSidePaginationRequest);
        serverSidePaginationResponse.setData(loanList);
        serverSidePaginationResponse.setLoadedRecords(loanList.size());
        serverSidePaginationResponse.setTotalRecords(this.financeDao.getLoanList_SSP_TotalRecords(serverSidePaginationRequest));
        return serverSidePaginationResponse;
    }

    public ServerSidePaginationResponse<FinancePayment> getPaymentList_SSP(ServerSidePaginationRequest<FinancePayment> serverSidePaginationRequest) {
        ServerSidePaginationResponse<FinancePayment> serverSidePaginationResponse = new ServerSidePaginationResponse<>();
        serverSidePaginationResponse.setServerSidePaginationRequest(serverSidePaginationRequest);
        List<FinancePayment> paymentList = this.financeDao.getPaymentList_SSP(serverSidePaginationRequest);
        serverSidePaginationResponse.setData(paymentList);
        serverSidePaginationResponse.setLoadedRecords(paymentList.size());
        serverSidePaginationResponse.setTotalRecords(this.financeDao.getPaymentList_SSP_TotalRecords(serverSidePaginationRequest));
        return serverSidePaginationResponse;
    }

    public Double getTotalCommitted() {
        return this.financeDao.getTotalCommitted();
    }

    public Double getTotalPaid() {
        return this.financeDao.getTotalPaid();
    }
}
