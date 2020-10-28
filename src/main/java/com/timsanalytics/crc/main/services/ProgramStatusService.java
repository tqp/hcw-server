package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.main.beans.ProgramStatus;
import com.timsanalytics.crc.main.dao.ProgramStatusDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramStatusService {
    private final ProgramStatusDao programStatusDao;

    @Autowired
    public ProgramStatusService(ProgramStatusDao programStatusDao) {
        this.programStatusDao = programStatusDao;
    }

    public List<ProgramStatus> getProgramStatusList() {
        return this.programStatusDao.getProgramStatusList();
    }

    public List<ProgramStatus> getProgramStatusTopLevelList() {
        return this.programStatusDao.getProgramStatusTopLevelList();
    }

    public List<ProgramStatus> getProgramStatusChildList(Integer parentId) {
        return this.programStatusDao.getProgramStatusChildList(parentId);
    }
}
