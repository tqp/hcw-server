package com.timsanalytics.crc.main.services.types;

import com.timsanalytics.crc.main.beans.types.PostGradEventType;
import com.timsanalytics.crc.main.dao.types.PostGradEventTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostGradEventTypeService {
    private final PostGradEventTypeDao postGradEventTypeDao;

    @Autowired
    public PostGradEventTypeService(PostGradEventTypeDao postGradEventTypeDao) {
        this.postGradEventTypeDao = postGradEventTypeDao;
    }

    public List<PostGradEventType> getPostGradEventTypeList() {
        return this.postGradEventTypeDao.getPostGradEventTypeList();
    }
}
