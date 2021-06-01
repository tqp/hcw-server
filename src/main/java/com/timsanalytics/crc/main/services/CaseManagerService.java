package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.CaseManager;
import com.timsanalytics.crc.main.dao.people.CaseManagerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaseManagerService {
    private final CaseManagerDao caseManagerDao;

    @Autowired
    public CaseManagerService(CaseManagerDao caseManagerDao) {
        this.caseManagerDao = caseManagerDao;
    }

    // BASIC CRUD

    public CaseManager createCaseManager(CaseManager caseManager) {
        return this.caseManagerDao.createCaseManager(caseManager);
    }

    public List<CaseManager> getCaseManagerList() {
        return this.caseManagerDao.getCaseManagerList();
    }

    public ServerSidePaginationResponse<CaseManager> getCaseManagerList_SSP(ServerSidePaginationRequest<CaseManager> serverSidePaginationRequest) {
        ServerSidePaginationResponse<CaseManager> serverSidePaginationResponse = new ServerSidePaginationResponse<CaseManager>();
        serverSidePaginationResponse.setServerSidePaginationRequest(serverSidePaginationRequest);
        List<CaseManager> caseManagerList = this.caseManagerDao.getCaseManagerList_SSP(serverSidePaginationRequest);
        serverSidePaginationResponse.setData(caseManagerList);
        serverSidePaginationResponse.setLoadedRecords(caseManagerList.size());
        serverSidePaginationResponse.setTotalRecords(this.caseManagerDao.getCaseManagerList_SSP_TotalRecords(serverSidePaginationRequest));
        return serverSidePaginationResponse;
    }

    public CaseManager getCaseManagerDetail(int userId) {
        return this.caseManagerDao.getCaseManagerDetail(userId);
    }

    public CaseManager updateCaseManager(CaseManager caseManager) {
        return this.caseManagerDao.updateCaseManager(caseManager);
    }

    public KeyValue deleteCaseManager(String caseManagerId) {
        return this.caseManagerDao.deleteCaseManager(caseManagerId);
    }

    // JOINED QUERIES

    public CaseManager getCurrentCaseManagerDetailByStudentId(int studentId) {
        return this.caseManagerDao.getCurrentCaseManagerDetailByStudentId(studentId);
    }
}
