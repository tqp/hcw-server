package com.timsanalytics.crc.admin.services;

import com.timsanalytics.crc.admin.dao.AppDao;
import com.timsanalytics.crc.main.dao.CaregiverDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class AppService {
    private final Environment environment;
    private final AppDao appDao;

    @Autowired
    public AppService(Environment environment, AppDao appDao) {
        this.environment = environment;
        this.appDao = appDao;
    }

    public Integer getDatabaseHealthCheck() {
        return this.appDao.getUserCount();
    }

    public String getBuildTimestamp() {
        return this.environment.getProperty("application.buildTimestamp");
    }
}
