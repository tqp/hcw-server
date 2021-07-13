package com.timsanalytics.crc.main.services.events;

import com.timsanalytics.crc.auth.authCommon.beans.User;
import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Student;
import com.timsanalytics.crc.main.beans.Visit;
import com.timsanalytics.crc.main.dao.events.VisitDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitService {
    private final VisitDao visitDao;

    @Autowired
    public VisitService(VisitDao visitDao) {
        this.visitDao = visitDao;
    }

    // BASIC CRUD

    public Visit createVisit(Visit visit) {
        return this.visitDao.createVisit(visit);
    }

    public List<Visit> getVisitList() {
        return this.visitDao.getVisitList();
    }

    public List<Visit> getVisitListByCaseManager(User loggedInUser) {
        return this.visitDao.getVisitListByCaseManager(loggedInUser);
    }

    public ServerSidePaginationResponse<Visit> getVisitList_SSP(ServerSidePaginationRequest<Visit> serverSidePaginationRequest) {
        ServerSidePaginationResponse<Visit> serverSidePaginationResponse = new ServerSidePaginationResponse<>();
        serverSidePaginationResponse.setServerSidePaginationRequest(serverSidePaginationRequest);
        List<Visit> visitList = this.visitDao.getVisitList_SSP(serverSidePaginationRequest);
        serverSidePaginationResponse.setData(visitList);
        serverSidePaginationResponse.setLoadedRecords(visitList.size());
        serverSidePaginationResponse.setTotalRecords(this.visitDao.getVisitList_SSP_TotalRecords(serverSidePaginationRequest));
        return serverSidePaginationResponse;
    }

    public Visit getVisitDetail(int visitId) {
        return this.visitDao.getVisitDetail(visitId);
    }

    public Visit updateVisit(Visit visit) {
        return this.visitDao.updateVisit(visit);
    }

    public KeyValue deleteVisit(String visitId) {
        return this.visitDao.deleteVisit(visitId);
    }

    // JOINED TABLES

    public List<Visit> getVisitListByStudentId(Integer studentId) {
        return this.visitDao.getVisitListByStudentId(studentId);
    }

    public List<Visit> getVisitListByCaseManagerAndStudent(User loggedInUser, Integer studentId) {
        return this.visitDao.getVisitListByCaseManagerAndStudent(loggedInUser, studentId);
    }
}
