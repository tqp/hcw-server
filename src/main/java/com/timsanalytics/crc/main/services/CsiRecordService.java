package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.CsiRecord;
import com.timsanalytics.crc.main.dao.events.CsiRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CsiRecordService {
    private final CsiRecordDao csiRecordDao;

    @Autowired
    public CsiRecordService(CsiRecordDao csiRecordDao) {
        this.csiRecordDao = csiRecordDao;
    }

    // BASIC CRUD

    public CsiRecord createCsiRecord(CsiRecord csiRecord) {
        return this.csiRecordDao.createCsiRecord(csiRecord);
    }

    public List<CsiRecord> getCsiRecordList() {
        return this.csiRecordDao.getCsiRecordList();
    }

    public ServerSidePaginationResponse<CsiRecord> getCsiRecordList_SSP(ServerSidePaginationRequest<CsiRecord> serverSidePaginationRequest) {
        ServerSidePaginationResponse<CsiRecord> serverSidePaginationResponse = new ServerSidePaginationResponse<>();
        serverSidePaginationResponse.setServerSidePaginationRequest(serverSidePaginationRequest);
        List<CsiRecord> csiRecordList = this.csiRecordDao.getCsiList_SSP(serverSidePaginationRequest);
        serverSidePaginationResponse.setData(csiRecordList);
        serverSidePaginationResponse.setLoadedRecords(csiRecordList.size());
        serverSidePaginationResponse.setTotalRecords(this.csiRecordDao.getCsiRecordList_SSP_TotalRecords(serverSidePaginationRequest));
        return serverSidePaginationResponse;
    }

    public CsiRecord getCsiRecordDetail(int csiRecordId) {
        return this.csiRecordDao.getCsiRecordDetail(csiRecordId);
    }

    public CsiRecord updateCsiRecord(CsiRecord csiRecord) {
        return this.csiRecordDao.updateCsiRecord(csiRecord);
    }

    public KeyValue deleteCsiRecord(String csiRecordId) {
        return this.csiRecordDao.deleteCsiRecord(csiRecordId);
    }

    // JOINED TABLES

    public List<CsiRecord> getCsiRecordListByStudentId(Integer studentId) {
        return this.csiRecordDao.getCsiRecordListByStudentId(studentId);
    }

    public CsiRecord getMostRecentCsiRecordScoresByStudentId(Integer studentId) {
        return this.csiRecordDao.getMostRecentCsiRecordScoresByStudentId(studentId);
    }

    public List<CsiRecord> getCsiRecordListByCaseManagerId(Integer caseManagerId) {
        return this.csiRecordDao.getCsiRecordListByCaseManagerId(caseManagerId);
    }
}
