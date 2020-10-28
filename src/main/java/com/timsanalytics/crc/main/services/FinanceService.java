package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Loan;
import com.timsanalytics.crc.main.beans.Payment;
import com.timsanalytics.crc.main.beans.Student;
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

    public ServerSidePaginationResponse<Loan> getLoanList_SSP(ServerSidePaginationRequest<Loan> serverSidePaginationRequest) {
        ServerSidePaginationResponse<Loan> serverSidePaginationResponse = new ServerSidePaginationResponse<>();
        serverSidePaginationResponse.setServerSidePaginationRequest(serverSidePaginationRequest);
        List<Loan> loanList = this.financeDao.getLoanList_SSP(serverSidePaginationRequest);
        serverSidePaginationResponse.setData(loanList);
        serverSidePaginationResponse.setLoadedRecords(loanList.size());
        serverSidePaginationResponse.setTotalRecords(this.financeDao.getLoanList_SSP_TotalRecords(serverSidePaginationRequest));
        return serverSidePaginationResponse;
    }

    public ServerSidePaginationResponse<Payment> getPaymentList_SSP(ServerSidePaginationRequest<Payment> serverSidePaginationRequest) {
        ServerSidePaginationResponse<Payment> serverSidePaginationResponse = new ServerSidePaginationResponse<>();
        serverSidePaginationResponse.setServerSidePaginationRequest(serverSidePaginationRequest);
        List<Payment> paymentList = this.financeDao.getPaymentList_SSP(serverSidePaginationRequest);
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

    public List<Loan> getLoanListByCaregiverId(Integer caregiverId) {
        return this.financeDao.getLoanListByCaregiverId(caregiverId);
    }

    public Payment addPayment(Payment payment) {
        return this.financeDao.addPayment(payment);
    }
}
