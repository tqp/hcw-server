package com.timsanalytics.crc.main.services.reports;

import com.timsanalytics.crc.main.beans.SummaryReportResult;
import com.timsanalytics.crc.main.dao.reports.SummaryReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SummaryReportService {
    private final SummaryReportDao summaryReportDao;

    @Autowired
    public SummaryReportService(SummaryReportDao summaryReportDao) {
        this.summaryReportDao = summaryReportDao;
    }

    public Integer getActiveStudents_Count() {
        return this.summaryReportDao.getActiveStudents_Count();
    }

    public List<SummaryReportResult> getActiveStudents_Results() {
        return this.summaryReportDao.getActiveStudents_Results();
    }
}
