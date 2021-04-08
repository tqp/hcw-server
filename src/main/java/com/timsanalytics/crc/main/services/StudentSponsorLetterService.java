package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.main.beans.StudentSponsorLetter;
import com.timsanalytics.crc.main.dao.StudentSponsorLetterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentSponsorLetterService {
    private final StudentSponsorLetterDao studentSponsorLetterDao;

    @Autowired
    public StudentSponsorLetterService(StudentSponsorLetterDao studentSponsorLetterDao) {
        this.studentSponsorLetterDao = studentSponsorLetterDao;
    }

    // BASIC CRUD

    // JOINED TABLES

    public List<StudentSponsorLetter> getStudentSponsorLetterListByStudentId(Integer studentId) {
        return this.studentSponsorLetterDao.getStudentSponsorLetterListByStudentId(studentId);
    }

    public List<StudentSponsorLetter> getStudentSponsorLetterListBySponsorId(Integer studentId) {
        return this.studentSponsorLetterDao.getStudentSponsorLetterListBySponsorId(studentId);
    }
}
