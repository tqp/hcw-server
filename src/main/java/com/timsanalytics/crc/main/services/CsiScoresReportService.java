package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.main.beans.CsiScoresReport;
import com.timsanalytics.crc.main.dao.CsiScoresReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CsiScoresReportService {
    private final CsiScoresReportDao csiScoresReportDao;

    @Autowired
    public CsiScoresReportService(CsiScoresReportDao csiScoresReportDao) {
        this.csiScoresReportDao = csiScoresReportDao;
    }

    public List<CsiScoresReport> getCsiScoresReportData() {
        return this.csiScoresReportDao.getCsiScoresReportData();
    }
}
