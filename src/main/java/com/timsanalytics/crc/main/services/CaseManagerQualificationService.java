package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.common.beans.KeyValueInteger;
import com.timsanalytics.crc.main.beans.CaseManagerQualification;
import com.timsanalytics.crc.main.dao.events.CaseManagerQualificationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaseManagerQualificationService {
    private final CaseManagerQualificationDao CaseManagerQualificationDao;

    @Autowired
    public CaseManagerQualificationService(CaseManagerQualificationDao CaseManagerQualificationDao) {
        this.CaseManagerQualificationDao = CaseManagerQualificationDao;
    }

    public CaseManagerQualification createCaseManagerQualification(String username, CaseManagerQualification CaseManagerQualification) {
        return this.CaseManagerQualificationDao.createWorkshopEntry(username, CaseManagerQualification);
    }

    public CaseManagerQualification getCaseManagerQualificationDetail(int CaseManagerQualificationId) {
        return this.CaseManagerQualificationDao.getCaseManagerQualificationDetail(CaseManagerQualificationId);
    }

    public CaseManagerQualification updateCaseManagerQualification(CaseManagerQualification CaseManagerQualification) {
        return this.CaseManagerQualificationDao.updateCaseManagerQualification(CaseManagerQualification);
    }

    public KeyValueInteger deleteCaseManagerQualification(int CaseManagerQualificationId) {
        return this.CaseManagerQualificationDao.deleteCaseManagerQualification(CaseManagerQualificationId);
    }

    public List<CaseManagerQualification> getQualificationListByCaseManagerId(int caseManagerId) {
        return this.CaseManagerQualificationDao.getQualificationListByCaseManagerId(caseManagerId);
    }
}
