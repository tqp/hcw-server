package com.timsanalytics.hcw.main.services;

import com.timsanalytics.hcw.common.beans.KeyValue;
import com.timsanalytics.hcw.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.hcw.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.hcw.main.beans.Caregiver;
import com.timsanalytics.hcw.main.dao.CaregiverDao;
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

    public Caregiver createCaregiver(Caregiver caregiver) {
        return this.caregiverDao.createCaregiver(caregiver);
    }

    public ServerSidePaginationResponse<Caregiver> getCaregiverList_SSP(ServerSidePaginationRequest<Caregiver> serverSidePaginationRequest) {
        ServerSidePaginationResponse<Caregiver> serverSidePaginationResponse = new ServerSidePaginationResponse<Caregiver>();
        serverSidePaginationResponse.setServerSidePaginationRequest(serverSidePaginationRequest);
        List<Caregiver> caregiverList = this.caregiverDao.getCaregiverList_SSP(serverSidePaginationRequest);
        serverSidePaginationResponse.setData(caregiverList);
        serverSidePaginationResponse.setLoadedRecords(caregiverList.size());
        serverSidePaginationResponse.setTotalRecords(this.caregiverDao.getCaregiverList_SSP_TotalRecords(serverSidePaginationRequest));
        return serverSidePaginationResponse;
    }

    public Caregiver getCaregiverDetail(String caregiverGuid) {
        return this.caregiverDao.getCaregiverDetail(caregiverGuid);
    }

    public Caregiver updateCaregiver(Caregiver caregiver) {
        return this.caregiverDao.updateCaregiver(caregiver);
    }

    public KeyValue deleteCaregiver(String caregiverGuid) {
        return this.caregiverDao.deleteCaregiver(caregiverGuid);
    }
}
