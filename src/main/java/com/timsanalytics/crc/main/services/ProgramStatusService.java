package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.main.beans.ProgramStatus;
import com.timsanalytics.crc.main.beans.ProgramStatusPackage;
import com.timsanalytics.crc.main.beans.Sponsor;
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

    public ProgramStatusPackage getProgramStatusPackage(Integer parentId) {
        return this.programStatusDao.getProgramStatusPackage(parentId);
    }

    public List<ProgramStatusPackage> getProgramStatusChildList(Integer parentId) {
        return this.programStatusDao.getProgramStatusChildList(parentId);
    }

    // JOINED QUERIES

    public ProgramStatus getProgramStatusDetailByStudentId(int studentId) {
        return this.programStatusDao.getProgramStatusDetailByStudentId(studentId);
    }
}
