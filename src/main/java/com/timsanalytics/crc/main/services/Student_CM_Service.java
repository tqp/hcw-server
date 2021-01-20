package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Student;
import com.timsanalytics.crc.main.dao.Student_CM_Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Student_CM_Service {
    private final Student_CM_Dao studentDao;

    @Autowired
    public Student_CM_Service(Student_CM_Dao studentDao) {
        this.studentDao = studentDao;
    }

    public List<Student> getStudentList() {
        return this.studentDao.getStudentList();
    }

    public ServerSidePaginationResponse<Student> getStudentList_SSP(ServerSidePaginationRequest<Student> serverSidePaginationRequest, Integer userId) {
        ServerSidePaginationResponse<Student> serverSidePaginationResponse = new ServerSidePaginationResponse<Student>();
        serverSidePaginationResponse.setServerSidePaginationRequest(serverSidePaginationRequest);
        List<Student> studentList = this.studentDao.getStudentList_SSP(serverSidePaginationRequest, userId);
        serverSidePaginationResponse.setData(studentList);
        serverSidePaginationResponse.setLoadedRecords(studentList.size());
        serverSidePaginationResponse.setTotalRecords(this.studentDao.getStudentList_SSP_TotalRecords(serverSidePaginationRequest, userId));
        return serverSidePaginationResponse;
    }

    public Student getStudentDetail(Integer studentId) {
        return this.studentDao.getStudentDetail(studentId);
    }

    public Student updateStudent(Student student) {
        return this.studentDao.updateStudent(student);
    }
}
