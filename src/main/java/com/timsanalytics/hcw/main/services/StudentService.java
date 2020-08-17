package com.timsanalytics.hcw.main.services;

import com.timsanalytics.hcw.common.beans.KeyValue;
import com.timsanalytics.hcw.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.hcw.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.hcw.main.beans.Student;
import com.timsanalytics.hcw.main.dao.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentDao studentDao;

    @Autowired
    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public Student createStudent(Student student) {
        return this.studentDao.createStudent(student);
    }

    public ServerSidePaginationResponse<Student> getStudentList_SSP(ServerSidePaginationRequest<Student> serverSidePaginationRequest) {
        ServerSidePaginationResponse<Student> serverSidePaginationResponse = new ServerSidePaginationResponse<Student>();
        serverSidePaginationResponse.setServerSidePaginationRequest(serverSidePaginationRequest);
        List<Student> studentList = this.studentDao.getStudentList_SSP(serverSidePaginationRequest);
        serverSidePaginationResponse.setData(studentList);
        serverSidePaginationResponse.setLoadedRecords(studentList.size());
        serverSidePaginationResponse.setTotalRecords(this.studentDao.getStudentList_SSP_TotalRecords(serverSidePaginationRequest));
        return serverSidePaginationResponse;
    }

    public Student getStudentDetail(String studentGuid) {
        return this.studentDao.getStudentDetail(studentGuid);
    }

    public Student updateStudent(Student student) {
        return this.studentDao.updateStudent(student);
    }

    public KeyValue deleteStudent(String studentGuid) {
        return this.studentDao.deleteStudent(studentGuid);
    }
}
