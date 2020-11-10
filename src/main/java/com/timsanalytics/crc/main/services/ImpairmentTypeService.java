package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.main.beans.ImpairmentType;
import com.timsanalytics.crc.main.beans.InteractionType;
import com.timsanalytics.crc.main.dao.ImpairmentTypeDao;
import com.timsanalytics.crc.main.dao.InteractionTypeDao;
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
