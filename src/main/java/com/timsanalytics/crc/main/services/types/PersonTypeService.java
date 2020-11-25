package com.timsanalytics.crc.main.services.types;

import com.timsanalytics.crc.main.beans.types.PersonType;
import com.timsanalytics.crc.main.dao.types.PersonTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonTypeService {
    private final PersonTypeDao personTypeDao;

    @Autowired
    public PersonTypeService(PersonTypeDao personTypeDao) {
        this.personTypeDao = personTypeDao;
    }

    public List<PersonType> getPersonTypeList() {
        return this.personTypeDao.getPersonTypeList();
    }
}

