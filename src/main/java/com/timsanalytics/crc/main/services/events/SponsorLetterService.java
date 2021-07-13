package com.timsanalytics.crc.main.services.events;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.main.beans.PostGradEvent;
import com.timsanalytics.crc.main.beans.SponsorLetter;
import com.timsanalytics.crc.main.beans.Visit;
import com.timsanalytics.crc.main.dao.events.SponsorLetterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SponsorLetterService {
    private final SponsorLetterDao sponsorLetterDao;

    @Autowired
    public SponsorLetterService(SponsorLetterDao sponsorLetterDao) {
        this.sponsorLetterDao = sponsorLetterDao;
    }

    // BASIC CRUD

    public SponsorLetter createSponsorLetter(SponsorLetter sponsorLetter) {
        return this.sponsorLetterDao.createSponsorLetter(sponsorLetter);
    }

    public List<SponsorLetter> getSponsorLetterList() {
        return this.sponsorLetterDao.getSponsorLetterList();
    }

    public SponsorLetter getSponsorLetterDetail(int studentSponsorLetterId) {
        return this.sponsorLetterDao.getSponsorLetterDetail(studentSponsorLetterId);
    }

    public SponsorLetter updateSponsorLetter(SponsorLetter sponsorLetter) {
        return this.sponsorLetterDao.updateSponsorLetter(sponsorLetter);
    }

    public KeyValue deleteSponsorLetter(String sponsorLetterId) {
        return this.sponsorLetterDao.deleteSponsorLetter(sponsorLetterId);
    }

    // JOINED TABLES

    public List<SponsorLetter> getSponsorLetterListByStudentId(Integer studentId) {
        return this.sponsorLetterDao.getSponsorLetterListByStudentId(studentId);
    }

    public List<SponsorLetter> getSponsorLetterListBySponsorId(Integer studentId) {
        return this.sponsorLetterDao.getSponsorLetterListBySponsorId(studentId);
    }
}
