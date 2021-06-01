package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.main.beans.CaseManagerRevised;
import com.timsanalytics.crc.main.dao.CaseManagerRevisedDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaseManagerRevisedService {
    private final CaseManagerRevisedDao caseManagerRevisedDao;

    @Autowired
    public CaseManagerRevisedService(CaseManagerRevisedDao caseManagerRevisedDao) {
        this.caseManagerRevisedDao = caseManagerRevisedDao;
    }

    // BASIC CRUD

    public List<CaseManagerRevised> getCaseManagerRevisedList() {
        return this.caseManagerRevisedDao.getCaseManagerRevisedList();
    }
}
