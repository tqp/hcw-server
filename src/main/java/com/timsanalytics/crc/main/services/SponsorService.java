package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Sponsor;
import com.timsanalytics.crc.main.dao.SponsorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SponsorService {
    private final SponsorDao sponsorDao;

    @Autowired
    public SponsorService(SponsorDao sponsorDao) {
        this.sponsorDao = sponsorDao;
    }

    // BASIC CRUD

    public Sponsor createSponsor(Sponsor sponsor) {
        return this.sponsorDao.createSponsor(sponsor);
    }

    public List<Sponsor> getSponsorList() {
        return this.sponsorDao.getSponsorList();
    }

    public ServerSidePaginationResponse<Sponsor> getSponsorList_SSP(ServerSidePaginationRequest<Sponsor> serverSidePaginationRequest) {
        ServerSidePaginationResponse<Sponsor> serverSidePaginationResponse = new ServerSidePaginationResponse<>();
        serverSidePaginationResponse.setServerSidePaginationRequest(serverSidePaginationRequest);
        List<Sponsor> sponsorList = this.sponsorDao.getSponsorList_SSP(serverSidePaginationRequest);
        serverSidePaginationResponse.setData(sponsorList);
        serverSidePaginationResponse.setLoadedRecords(sponsorList.size());
        serverSidePaginationResponse.setTotalRecords(this.sponsorDao.getSponsorList_SSP_TotalRecords(serverSidePaginationRequest));
        return serverSidePaginationResponse;
    }

    public Sponsor getSponsorDetail(int sponsorId) {
        return this.sponsorDao.getSponsorDetail(sponsorId);
    }

    public Sponsor updateSponsor(Sponsor sponsor) {
        return this.sponsorDao.updateSponsor(sponsor);
    }

    public KeyValue deleteSponsor(String sponsorGuid) {
        return this.sponsorDao.deleteSponsor(sponsorGuid);
    }

    // JOINED QUERIES

    public Sponsor getSponsorDetailByStudentId(int studentId) {
        return this.sponsorDao.getSponsorDetailByStudentId(studentId);
    }

}
