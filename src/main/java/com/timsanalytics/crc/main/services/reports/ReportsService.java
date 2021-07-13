package com.timsanalytics.crc.main.services.reports;

import com.timsanalytics.crc.main.beans.Student;
import com.timsanalytics.crc.main.dao.reports.ReportsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportsService {
    private final ReportsDao reportsDao;

    @Autowired
    public ReportsService(ReportsDao reportsDao) {
        this.reportsDao = reportsDao;
    }

    public List<Student> getCaseManagerCoverageReport() {
        return this.reportsDao.getCaseManagerCoverageReport();
    }

    public List<Student> getCaregiverCoverageReport() {
        return this.reportsDao.getCaregiverCoverageReport();
    }


}
