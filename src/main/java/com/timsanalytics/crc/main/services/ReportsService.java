package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.main.dao.ReportsDao;
import com.timsanalytics.crc.main.dao.SummaryReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportsService {
    private final ReportsDao reportsDao;

    @Autowired
    public ReportsService(ReportsDao reportsDao) {
        this.reportsDao = reportsDao;
    }

    public Integer getCaseManagerCoverageReport() {
//        return this.reportsDao.getCaseManagerCoverageReport();
        return null;
    }


}
