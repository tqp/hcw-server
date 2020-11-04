package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Loan;
import com.timsanalytics.crc.main.beans.Payment;
import com.timsanalytics.crc.main.dao.PaymentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    private final PaymentDao paymentDao;

    @Autowired
    public PaymentService(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }

    public Payment addPayment(Payment payment) {
        return this.paymentDao.createPayment(payment);
    }

    public ServerSidePaginationResponse<Payment> getPaymentList_SSP(ServerSidePaginationRequest<Payment> serverSidePaginationRequest) {
        ServerSidePaginationResponse<Payment> serverSidePaginationResponse = new ServerSidePaginationResponse<>();
        serverSidePaginationResponse.setServerSidePaginationRequest(serverSidePaginationRequest);
        List<Payment> paymentList = this.paymentDao.getPaymentList_SSP(serverSidePaginationRequest);
        serverSidePaginationResponse.setData(paymentList);
        serverSidePaginationResponse.setLoadedRecords(paymentList.size());
        serverSidePaginationResponse.setTotalRecords(this.paymentDao.getPaymentList_SSP_TotalRecords(serverSidePaginationRequest));
        return serverSidePaginationResponse;
    }

    public Payment getPaymentDetail(int paymentId) {
        return this.paymentDao.getPaymentDetail(paymentId);
    }

    public Payment updatePayment(Payment payment) {
        return this.paymentDao.updatePayment(payment);
    }

    public KeyValue deletePayment(String paymentId) {
        return this.paymentDao.deletePayment(paymentId);
    }


}
