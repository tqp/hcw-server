package com.timsanalytics.crc.main.services.types;

import com.timsanalytics.crc.main.beans.types.TierType;
import com.timsanalytics.crc.main.dao.types.TierTypeDao;
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
