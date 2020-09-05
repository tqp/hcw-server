package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.main.beans.Relationship;
import com.timsanalytics.crc.main.beans.Student;
import com.timsanalytics.crc.main.dao.RelationshipDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationshipService {
    private final RelationshipDao relationshipDao;

    @Autowired
    public RelationshipService(RelationshipDao relationshipDao) {
        this.relationshipDao = relationshipDao;
    }

    public Relationship createRelationship(Relationship relationship) {
        return this.relationshipDao.createRelationship(relationship);
    }

    public List<Relationship> getRelationshipListByStudentId(Integer studentId) {
        return this.relationshipDao.getRelationshipListByStudentId(studentId);
    }

    // OTHER

    public Relationship createRelationshipPerson(Relationship relationship) {
        return this.relationshipDao.createRelationshipPerson(relationship);
    }
}
