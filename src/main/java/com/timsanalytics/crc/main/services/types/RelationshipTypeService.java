package com.timsanalytics.crc.main.services.types;

import com.timsanalytics.crc.main.beans.types.RelationshipType;
import com.timsanalytics.crc.main.dao.types.RelationshipTypeDao;
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

