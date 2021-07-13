package com.timsanalytics.crc.main.services.reports;

import com.timsanalytics.crc.main.dao.reports.SummaryReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SummaryReportService {
    private final SummaryReportDao summaryReportDao;

    @Autowired
    public SummaryReportService(SummaryReportDao summaryReportDao) {
        this.summaryReportDao = summaryReportDao;
    }

    public Integer getStudentCountTotal() {
        return this.summaryReportDao.getStudentCountTotal();
    }

    public Integer getStudentCountReintegrated() {
        return this.summaryReportDao.getStudentCountReintegrated();
    }

    public Integer getStudentCountReintegratedRunaway() {
        return this.summaryReportDao.getStudentCountReintegratedRunaway();
    }

    public Integer getStudentCountFamiliesIntact() {
        return this.summaryReportDao.getStudentCountFamiliesIntact();
    }

    public Integer getStudentCountFamiliesIntactEnrolled() {
        return this.summaryReportDao.getStudentCountFamiliesIntactEnrolled();
    }

}
