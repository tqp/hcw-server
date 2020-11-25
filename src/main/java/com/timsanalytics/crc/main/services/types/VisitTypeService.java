package com.timsanalytics.crc.main.services.types;

import com.timsanalytics.crc.main.beans.types.VisitType;
import com.timsanalytics.crc.main.dao.types.VisitTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitTypeService {
    private final VisitTypeDao visitTypeDao;

    @Autowired
    public VisitTypeService(VisitTypeDao visitTypeDao) {
        this.visitTypeDao = visitTypeDao;
    }

    public List<VisitType> getVisitTypeList() {
        return this.visitTypeDao.getVisitTypeList();
    }
}
