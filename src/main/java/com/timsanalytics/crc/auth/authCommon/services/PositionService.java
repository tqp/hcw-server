package com.timsanalytics.crc.auth.authCommon.services;

import com.timsanalytics.crc.auth.authCommon.beans.Position;
import com.timsanalytics.crc.auth.authCommon.dao.PositionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {
    private final PositionDao positionDao;

    @Autowired
    public PositionService(PositionDao positionDao) {
        this.positionDao = positionDao;
    }

    public List<Position> getPositionList() {
        return this.positionDao.getPositionList();
    }
}
