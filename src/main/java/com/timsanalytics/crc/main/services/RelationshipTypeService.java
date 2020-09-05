package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.main.beans.RelationshipType;
import com.timsanalytics.crc.main.dao.RelationshipTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationshipTypeService {
    private final RelationshipTypeDao relationshipTypeDao;

    @Autowired
    public RelationshipTypeService(RelationshipTypeDao relationshipTypeDao) {
        this.relationshipTypeDao = relationshipTypeDao;
    }

    public List<RelationshipType> getRelationshipTypeList() {
        return this.relationshipTypeDao.getRelationshipTypeList();
    }
}

