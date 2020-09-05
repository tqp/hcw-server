package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.main.beans.Person;
import com.timsanalytics.crc.main.dao.PersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    private final PersonDao personDao;

    @Autowired
    public PersonService(PersonDao personDao) {
        this.personDao = personDao;
    }

    public Long createPerson_Relationship(Person person) {
        return this.personDao.createPerson_Relationship(person);
    }
}
