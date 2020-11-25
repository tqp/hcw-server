package com.timsanalytics.crc.main.services.types;

import com.timsanalytics.crc.main.beans.types.ImpairmentType;
import com.timsanalytics.crc.main.dao.types.ImpairmentTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImpairmentTypeService {
    private final ImpairmentTypeDao impairmentTypeDao;

    @Autowired
    public ImpairmentTypeService(ImpairmentTypeDao impairmentTypeDao) {
        this.impairmentTypeDao = impairmentTypeDao;
    }

    public List<ImpairmentType> getImpairmentTypeList() {
        return this.impairmentTypeDao.getImpairmentTypeList();
    }
}
