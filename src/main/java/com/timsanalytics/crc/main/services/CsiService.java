package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Csi;
import com.timsanalytics.crc.main.dao.CsiDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CsiService {
    private final CsiDao csiDao;

    @Autowired
    public CsiService(CsiDao csiDao) {
        this.csiDao = csiDao;
    }

    // BASIC CRUD

    public Csi createCsi(Csi csi) {
        return this.csiDao.createCsi(csi);
    }

    public List<Csi> getCsiList() {
        return this.csiDao.getCsiList();
    }

    public ServerSidePaginationResponse<Csi> getCsiList_SSP(ServerSidePaginationRequest<Csi> serverSidePaginationRequest) {
        ServerSidePaginationResponse<Csi> serverSidePaginationResponse = new ServerSidePaginationResponse<>();
        serverSidePaginationResponse.setServerSidePaginationRequest(serverSidePaginationRequest);
        List<Csi> csiList = this.csiDao.getCsiList_SSP(serverSidePaginationRequest);
        serverSidePaginationResponse.setData(csiList);
        serverSidePaginationResponse.setLoadedRecords(csiList.size());
        serverSidePaginationResponse.setTotalRecords(this.csiDao.getCsiList_SSP_TotalRecords(serverSidePaginationRequest));
        return serverSidePaginationResponse;
    }

    public Csi getCsiDetail(int csiRecordId) {
        return this.csiDao.getCsiDetail(csiRecordId);
    }

    public Csi updateCsi(Csi csi) {
        return this.csiDao.updateCsi(csi);
    }

    public KeyValue deleteCsi(String csiRecordId) {
        return this.csiDao.deleteCsi(csiRecordId);
    }

    // JOINED TABLES

    public List<Csi> getCsiListByStudentId(Integer studentId) {
        return this.csiDao.getCsiListByStudentId(studentId);
    }

    public Csi getMostRecentCsiScoresByStudentId(Integer studentId) {
        return this.csiDao.getMostRecentCsiScoresByStudentId(studentId);
    }

    public List<Csi> getCsiListByCaseManagerId(Integer caseManagerId) {
        return this.csiDao.getCsiListByCaseManagerId(caseManagerId);
    }
}
