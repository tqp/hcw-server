package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.main.beans.SponsorLetter;
import com.timsanalytics.crc.main.dao.events.StudentSponsorLetterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SponsorLetterService {
    private final StudentSponsorLetterDao studentSponsorLetterDao;

    @Autowired
    public SponsorLetterService(StudentSponsorLetterDao studentSponsorLetterDao) {
        this.studentSponsorLetterDao = studentSponsorLetterDao;
    }

    // BASIC CRUD

    public SponsorLetter getSponsorLetterDetail(int studentSponsorLetterId) {
        return this.studentSponsorLetterDao.getSponsorLetterDetail(studentSponsorLetterId);
    }

    public SponsorLetter updateSponsorLetter(SponsorLetter sponsorLetter) {
        return this.studentSponsorLetterDao.updateSponsorLetter(sponsorLetter);
    }

    // JOINED TABLES

    public List<SponsorLetter> getSponsorLetterListByStudentId(Integer studentId) {
        return this.studentSponsorLetterDao.getSponsorLetterListByStudentId(studentId);
    }

    public List<SponsorLetter> getSponsorLetterListBySponsorId(Integer studentId) {
        return this.studentSponsorLetterDao.getSponsorLetterListBySponsorId(studentId);
    }
}
