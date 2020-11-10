package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.main.beans.ProgramStatusPackage;
import com.timsanalytics.crc.main.beans.SchoolClassType;
import com.timsanalytics.crc.main.dao.ProgramStatusDao;
import com.timsanalytics.crc.main.dao.SchoolClassTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolClassTypeService {
    private final SchoolClassTypeDao schoolClassTypeDao;

    @Autowired
    public SchoolClassTypeService(SchoolClassTypeDao schoolClassTypeDao) {
        this.schoolClassTypeDao = schoolClassTypeDao;
    }

    public List<SchoolClassType> getSchoolClassTypeChildList(Integer parentId) {
        return this.schoolClassTypeDao.getSchoolClassTypeChildList(parentId);
    }
}
