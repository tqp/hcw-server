package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.common.beans.KeyValueInteger;
import com.timsanalytics.crc.main.beans.CaregiverWorkshop;
import com.timsanalytics.crc.main.dao.CaregiverWorkshopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaregiverWorkshopService {
    private final CaregiverWorkshopDao caregiverWorkshopDao;

    @Autowired
    public CaregiverWorkshopService(CaregiverWorkshopDao caregiverWorkshopDao) {
        this.caregiverWorkshopDao = caregiverWorkshopDao;
    }

    public CaregiverWorkshop createCaregiverWorkshop(String username, CaregiverWorkshop caregiverWorkshop) {
        return this.caregiverWorkshopDao.createWorkshopEntry(username, caregiverWorkshop);
    }

    public CaregiverWorkshop getCaregiverWorkshopDetail(int caregiverWorkshopId) {
        return this.caregiverWorkshopDao.getCaregiverWorkshopDetail(caregiverWorkshopId);
    }

    public CaregiverWorkshop updateCaregiverWorkshop(CaregiverWorkshop caregiverWorkshop) {
        return this.caregiverWorkshopDao.updateCaregiverWorkshop(caregiverWorkshop);
    }

    public KeyValueInteger deleteCaregiverWorkshop(int caregiverWorkshopId) {
        return this.caregiverWorkshopDao.deleteCaregiverWorkshop(caregiverWorkshopId);
    }

    public List<CaregiverWorkshop> getWorkshopListByCaregiverId(int caregiverId) {
        return this.caregiverWorkshopDao.getWorkshopListByCaregiverId(caregiverId);
    }


}
