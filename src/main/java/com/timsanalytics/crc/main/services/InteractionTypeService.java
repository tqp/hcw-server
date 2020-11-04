package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.main.beans.InteractionType;
import com.timsanalytics.crc.main.dao.InteractionTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InteractionTypeService {
    private final InteractionTypeDao interactionTypeDao;

    @Autowired
    public InteractionTypeService(InteractionTypeDao interactionTypeDao) {
        this.interactionTypeDao = interactionTypeDao;
    }

    public List<InteractionType> getInteractionTypeList() {
        return this.interactionTypeDao.getInteractionTypeList();
    }
}
