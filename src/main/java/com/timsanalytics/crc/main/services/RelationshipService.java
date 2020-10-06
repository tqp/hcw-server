package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.main.beans.Relationship;
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

    public Relationship createCaregiverRelationship(String username, Relationship relationship) {
        return this.relationshipDao.createCaregiverRelationship(username, relationship);
    }

    public Relationship createCaseManagerRelationship(String username, Relationship relationship) {
        return this.relationshipDao.createCaseManagerRelationship(username, relationship);
    }

    public Relationship createSponsorRelationship(String username, Relationship relationship) {
        return this.relationshipDao.createSponsorRelationship(username, relationship);
    }

    public List<Relationship> getRelationshipListByStudentId(Integer studentId) {
        return this.relationshipDao.getRelationshipListByStudentId(studentId);
    }

    public List<Relationship> getRelationshipListByRelationId(Integer relationId) {
        return this.relationshipDao.getRelationshipListByRelationId(relationId);
    }

    public List<Relationship> getRelationshipListByCaregiverId(Integer relationId) {
        return this.relationshipDao.getRelationshipListByCaregiverId(relationId);
    }

    // OTHER

    public Relationship createRelationshipPerson(Relationship relationship) {
        return this.relationshipDao.createRelationshipPerson(relationship);
    }
}
