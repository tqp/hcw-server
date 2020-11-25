package com.timsanalytics.crc.main.services.types;

import com.timsanalytics.crc.main.beans.types.SchoolClassType;
import com.timsanalytics.crc.main.dao.types.SchoolClassTypeDao;
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
