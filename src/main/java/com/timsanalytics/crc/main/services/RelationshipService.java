package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.KeyValueLong;
import com.timsanalytics.crc.main.beans.ProgramStatus;
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

    // CAREGIVER

    public Relationship createCaregiverRelationship(String username, Relationship relationship) {
        return this.relationshipDao.createCaregiverRelationship(username, relationship);
    }

    public List<Relationship> getStudentListByCaregiverId(Integer caregiverId) {
        return this.relationshipDao.getStudentListByCaregiverId(caregiverId);
    }

    public Relationship updateCaregiverRelationship(String username, Relationship relationship) {
        return this.relationshipDao.updateCaregiverRelationship(username, relationship);
    }

    public KeyValueLong deleteCaregiverRelationship(Integer relationshipId) {
        return this.relationshipDao.deleteCaregiverRelationship(relationshipId);
    }

    // CASE MANAGER

    public Relationship createCaseManagerRelationship(String username, Relationship relationship) {
        return this.relationshipDao.createCaseManagerRelationship(username, relationship);
    }

    public List<Relationship> getStudentListByCaseManagerId(Integer caseManagerId) {
        return this.relationshipDao.getStudentListByCaseManagerId(caseManagerId);
    }

    public Relationship updateCaseManagerRelationship(String username, Relationship relationship) {
        return this.relationshipDao.updateCaseManagerRelationship(username, relationship);
    }

    public KeyValueLong deleteCaseManagerRelationship(Integer relationshipId) {
        return this.relationshipDao.deleteCaseManagerRelationship(relationshipId);
    }

    // SPONSOR

    public Relationship createSponsorRelationship(String username, Relationship relationship) {
        return this.relationshipDao.createSponsorRelationship(username, relationship);
    }

    public List<Relationship> getStudentListBySponsorId(Integer sponsorId) {
        return this.relationshipDao.getStudentListBySponsorId(sponsorId);
    }

    public Relationship updateSponsorRelationship(String username, Relationship relationship) {
        return this.relationshipDao.updateSponsorRelationship(username, relationship);
    }

    public KeyValueLong deleteSponsorRelationship(Integer relationshipId) {
        return this.relationshipDao.deleteSponsorRelationship(relationshipId);
    }

    // PROGRAM STATUS

    public ProgramStatus createProgramStatusEntry(String username, ProgramStatus programStatus) {
        return this.relationshipDao.createProgramStatusEntry(username, programStatus);
    }


//    public List<StudentRelationship> getRelationshipListByStudentId(Integer studentId) {
//        return this.relationshipDao.getRelationshipListByStudentId(studentId);
//    }

//    public List<StudentRelationship> getRelationshipListByPersonId(Integer personId) {
//        return this.relationshipDao.getRelationshipListByPersonId(personId);
//    }

    // OTHER

//    public StudentRelationship createRelationshipPerson(StudentRelationship relationship) {
//        return this.relationshipDao.createRelationshipPerson(relationship);
//    }
}
