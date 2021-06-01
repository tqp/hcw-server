package com.timsanalytics.crc.main.services;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Student;
import com.timsanalytics.crc.main.dao.people.StudentDao;
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

    public List<Student> getStudentList() {
        return this.studentDao.getStudentList();
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

    public Student getStudentDetail(Integer studentId) {
        return this.studentDao.getStudentDetail(studentId);
    }

    public Student updateStudent(Student student) {
        return this.studentDao.updateStudent(student);
    }

    public KeyValue deleteStudent(String studentId) {
        return this.studentDao.deleteStudent(studentId);
    }

    // FILTERED

    public List<Student> getStudentListBySponsorId(Integer sponsorId) {
        return this.studentDao.getStudentListBySponsorId(sponsorId);
    }

    // OTHER QUERIES

    public List<Student> checkDuplicateStudentRecord(Student student) {
        return this.studentDao.checkDuplicateStudentRecord(student);
    }
}
