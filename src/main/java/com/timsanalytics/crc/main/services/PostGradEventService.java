package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.PostGradEvent;
import com.timsanalytics.crc.main.dao.events.PostGradEventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostGradEventService {
    private final PostGradEventDao postGradEventDao;

    @Autowired
    public PostGradEventService(PostGradEventDao postGradEventDao) {
        this.postGradEventDao = postGradEventDao;
    }

    // BASIC CRUD

    public PostGradEvent createPostGradEvent(PostGradEvent postGradEvent) {
        return this.postGradEventDao.createPostGradEvent(postGradEvent);
    }

    public List<PostGradEvent> getPostGradEventList() {
        return this.postGradEventDao.getPostGradEventList();
    }

    public ServerSidePaginationResponse<PostGradEvent> getPostGradEventList_SSP(ServerSidePaginationRequest<PostGradEvent> serverSidePaginationRequest) {
        ServerSidePaginationResponse<PostGradEvent> serverSidePaginationResponse = new ServerSidePaginationResponse<>();
        serverSidePaginationResponse.setServerSidePaginationRequest(serverSidePaginationRequest);
        List<PostGradEvent> postGradEventList = this.postGradEventDao.getPostGradEventList_SSP(serverSidePaginationRequest);
        serverSidePaginationResponse.setData(postGradEventList);
        serverSidePaginationResponse.setLoadedRecords(postGradEventList.size());
        serverSidePaginationResponse.setTotalRecords(this.postGradEventDao.getPostGradEventList_SSP_TotalRecords(serverSidePaginationRequest));
        return serverSidePaginationResponse;
    }

    public PostGradEvent getPostGradEventDetail(int postGradEventId) {
        return this.postGradEventDao.getPostGradEventDetail(postGradEventId);
    }

    public PostGradEvent updatePostGradEvent(PostGradEvent postGradEvent) {
        return this.postGradEventDao.updatePostGradEvent(postGradEvent);
    }

    public KeyValue deletePostGradEvent(String postGradEventId) {
        return this.postGradEventDao.deletePostGradEvent(postGradEventId);
    }

    // JOINED TABLES

    public List<PostGradEvent> getPostGradEventListByStudentId(Integer studentId) {
        return this.postGradEventDao.getPostGradEventListByStudentId(studentId);
    }
}
