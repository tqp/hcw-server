package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Caregiver;
import com.timsanalytics.crc.main.dao.CaregiverDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaregiverService {
    private final CaregiverDao caregiverDao;

    @Autowired
    public CaregiverService(CaregiverDao caregiverDao) {
        this.caregiverDao = caregiverDao;
    }

    // BASIC CRUD

    public Caregiver createCaregiver(Caregiver caregiver) {
        return this.caregiverDao.createCaregiver(caregiver);
    }

    public List<Caregiver> getCaregiverList() {
        return this.caregiverDao.getCaregiverList();
    }

    public ServerSidePaginationResponse<Caregiver> getCaregiverList_SSP(ServerSidePaginationRequest<Caregiver> serverSidePaginationRequest) {
        ServerSidePaginationResponse<Caregiver> serverSidePaginationResponse = new ServerSidePaginationResponse<>();
        serverSidePaginationResponse.setServerSidePaginationRequest(serverSidePaginationRequest);
        List<Caregiver> caregiverList = this.caregiverDao.getCaregiverList_SSP(serverSidePaginationRequest);
        serverSidePaginationResponse.setData(caregiverList);
        serverSidePaginationResponse.setLoadedRecords(caregiverList.size());
        serverSidePaginationResponse.setTotalRecords(this.caregiverDao.getCaregiverList_SSP_TotalRecords(serverSidePaginationRequest));
        return serverSidePaginationResponse;
    }

    public Caregiver getCaregiverDetail(int caregiverId) {
        return this.caregiverDao.getCaregiverDetail(caregiverId);
    }

    public Caregiver updateCaregiver(Caregiver caregiver) {
        return this.caregiverDao.updateCaregiver(caregiver);
    }

    public KeyValue deleteCaregiver(String caregiverGuid) {
        return this.caregiverDao.deleteCaregiver(caregiverGuid);
    }

    // JOINED QUERIES

    public Caregiver getCaregiverDetailByStudentId(int studentId) {
        return this.caregiverDao.getCaregiverDetailByStudentId(studentId);
    }

}
