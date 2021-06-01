package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.main.beans.Loan;
import com.timsanalytics.crc.main.dao.finance.FinanceDao;
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

    public Double getTotalCommitted() {
        return this.financeDao.getTotalCommitted();
    }

    public Double getTotalPaid() {
        return this.financeDao.getTotalPaid();
    }

    public List<Loan> getLoanListByCaregiverId(Integer caregiverId) {
        return this.financeDao.getLoanListByCaregiverId(caregiverId);
    }

}
