package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Relation;
import com.timsanalytics.crc.main.dao.RelationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationService {
    private final RelationDao relationDao;

    @Autowired
    public RelationService(RelationDao relationDao) {
        this.relationDao = relationDao;
    }

    public Relation createRelation(Relation relation) {
        return this.relationDao.createRelation(relation);
    }

    public ServerSidePaginationResponse<Relation> getRelationList_SSP(ServerSidePaginationRequest<Relation> serverSidePaginationRequest) {
        ServerSidePaginationResponse<Relation> serverSidePaginationResponse = new ServerSidePaginationResponse<Relation>();
        serverSidePaginationResponse.setServerSidePaginationRequest(serverSidePaginationRequest);
        List<Relation> relationList = this.relationDao.getRelationList_SSP(serverSidePaginationRequest);
        serverSidePaginationResponse.setData(relationList);
        serverSidePaginationResponse.setLoadedRecords(relationList.size());
        serverSidePaginationResponse.setTotalRecords(this.relationDao.getRelationList_SSP_TotalRecords(serverSidePaginationRequest));
        return serverSidePaginationResponse;
    }

    public Relation getRelationDetail(Integer relationGuid) {
        return this.relationDao.getRelationDetail(relationGuid);
    }

    public Relation updateRelation(Relation relation) {
        return this.relationDao.updateRelation(relation);
    }

    public KeyValue deleteRelation(String relationGuid) {
        return this.relationDao.deleteRelation(relationGuid);
    }

    public List<Relation> getRelationListBystudentId(String studentId) {
        return this.relationDao.getRelationListBystudentId(studentId);
    }
}
