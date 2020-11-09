package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.main.beans.ProgramStatus;
import com.timsanalytics.crc.main.beans.Student;
import com.timsanalytics.crc.main.beans.StudentRelationship;
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

    public StudentRelationship createCaregiverRelationship(String username, StudentRelationship relationship) {
        return this.relationshipDao.createCaregiverRelationship(username, relationship);
    }

    public List<StudentRelationship> getStudentListByCaregiverId(Integer caregiverId) {
        return this.relationshipDao.getStudentListByCaregiverId(caregiverId);
    }

    // CASE MANAGER

    public StudentRelationship createCaseManagerRelationship(String username, StudentRelationship relationship) {
        return this.relationshipDao.createCaseManagerRelationship(username, relationship);
    }

    public List<StudentRelationship> getStudentListByCaseManagerId(Integer caseManagerId) {
        return this.relationshipDao.getStudentListByCaseManagerId(caseManagerId);
    }

    // SPONSOR

    public StudentRelationship createSponsorRelationship(String username, StudentRelationship relationship) {
        return this.relationshipDao.createSponsorRelationship(username, relationship);
    }

    public List<StudentRelationship> getStudentListBySponsorId(Integer sponsorId) {
        return this.relationshipDao.getStudentListBySponsorId(sponsorId);
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
