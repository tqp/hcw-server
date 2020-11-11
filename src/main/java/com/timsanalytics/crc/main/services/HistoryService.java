package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.main.beans.History;
import com.timsanalytics.crc.main.dao.HistoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {
    private final HistoryDao historyDao;

    @Autowired
    public HistoryService(HistoryDao historyDao) {
        this.historyDao = historyDao;
    }

    public List<History> getHistoryListByStudentId(Integer studentId) {
        return this.historyDao.getHistoryListByStudentId(studentId);
    }
}
