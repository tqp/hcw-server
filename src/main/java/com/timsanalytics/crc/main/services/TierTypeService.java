package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.main.beans.TierType;
import com.timsanalytics.crc.main.dao.TierTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TierTypeService {
    private final TierTypeDao tierTypeDao;

    @Autowired
    public TierTypeService(TierTypeDao tierTypeDao) {
        this.tierTypeDao = tierTypeDao;
    }

    public List<TierType> getTierTypeList() {
        return this.tierTypeDao.getTierTypeList();
    }
}
