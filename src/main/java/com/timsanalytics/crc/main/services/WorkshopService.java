package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.main.beans.Workshop;
import com.timsanalytics.crc.main.dao.WorkshopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkshopService {
    private final WorkshopDao workshopDao;

    @Autowired
    public WorkshopService(WorkshopDao workshopDao) {
        this.workshopDao = workshopDao;
    }

    public List<Workshop> getWorkshopListByCaregiverId(int caregiverId) {
        return this.workshopDao.getWorkshopListByCaregiverId(caregiverId);
    }
}
