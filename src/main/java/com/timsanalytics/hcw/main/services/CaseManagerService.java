package com.timsanalytics.hcw.main.services;

import com.timsanalytics.hcw.common.beans.KeyValue;
import com.timsanalytics.hcw.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.hcw.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.hcw.main.beans.CaseManager;
import com.timsanalytics.hcw.main.dao.CaseManagerDao;
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

    public CaseManager createCaseManager(CaseManager caseManager) {
        return this.caseManagerDao.createCaseManager(caseManager);
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

    public CaseManager getCaseManagerDetail(String caseManagerGuid) {
        return this.caseManagerDao.getCaseManagerDetail(caseManagerGuid);
    }

    public CaseManager updateCaseManager(CaseManager caseManager) {
        return this.caseManagerDao.updateCaseManager(caseManager);
    }

    public KeyValue deleteCaseManager(String caseManagerGuid) {
        return this.caseManagerDao.deleteCaseManager(caseManagerGuid);
    }
}
