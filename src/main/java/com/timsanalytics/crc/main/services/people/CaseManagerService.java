package com.timsanalytics.crc.main.services.people;

import com.timsanalytics.crc.auth.authCommon.beans.User;
import com.timsanalytics.crc.auth.authCommon.services.UserService;
import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.CaseManager;
import com.timsanalytics.crc.main.dao.people.CaseManagerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@Service
public class CaseManagerService {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final CaseManagerDao caseManagerDao;
    private final UserService userService;
    private final PlatformTransactionManager mySqlAuthTransactionManager;

    @Autowired
    public CaseManagerService(CaseManagerDao caseManagerDao,
                              PlatformTransactionManager mySqlAuthTransactionManager,
                              UserService userService) {
        this.caseManagerDao = caseManagerDao;
        this.mySqlAuthTransactionManager = mySqlAuthTransactionManager;
        this.userService = userService;
    }

    // BASIC CRUD

    public CaseManager createCaseManager(CaseManager caseManager) {
        return this.caseManagerDao.createCaseManager(caseManager);
    }

    public List<CaseManager> getCaseManagerList() {
        return this.caseManagerDao.getCaseManagerList();
    }

    public ServerSidePaginationResponse<CaseManager> getCaseManagerList_SSP(ServerSidePaginationRequest<CaseManager> serverSidePaginationRequest) {
        ServerSidePaginationResponse<CaseManager> serverSidePaginationResponse = new ServerSidePaginationResponse<CaseManager>();
        serverSidePaginationResponse.setServerSidePaginationRequest(serverSidePaginationRequest);
        List<CaseManager> caseManagerList = this.caseManagerDao.getCaseManagerList_SSP(serverSidePaginationRequest);
        serverSidePaginationResponse.setData(caseManagerList);
        serverSidePaginationResponse.setLoadedRecords(caseManagerList.size());
        serverSidePaginationResponse.setTotalRecords(this.caseManagerDao.getCaseManagerList_SSP_TotalRecords(serverSidePaginationRequest));
        return serverSidePaginationResponse;
    }

    public CaseManager getCaseManagerDetail(int userId) {
        return this.caseManagerDao.getCaseManagerDetail(userId);
    }

    public CaseManager updateCaseManager(CaseManager caseManager, User loggedInUser) {
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = mySqlAuthTransactionManager.getTransaction(txDef);
        try {
            // Update the Case Manager Record
            this.caseManagerDao.updateCaseManager(caseManager);
            // Update the User Record
            this.userService.updateUserNameFromCaseManager(caseManager, loggedInUser);
            // If everything was successful, commit to the database.
            mySqlAuthTransactionManager.commit(txStatus);
        } catch (Exception e) {
            mySqlAuthTransactionManager.rollback(txStatus);
            logger.error("Error during Case Manager update. Case Manager User Id = " + caseManager.getCaseManagerUserId());
        }
        return caseManager;
    }

    public KeyValue deleteCaseManager(String caseManagerId) {
        return this.caseManagerDao.deleteCaseManager(caseManagerId);
    }

    // JOINED QUERIES

    public CaseManager getCurrentCaseManagerDetailByStudentId(int studentId) {
        return this.caseManagerDao.getCurrentCaseManagerDetailByStudentId(studentId);
    }

    public Boolean isTheLoggedInUserTheStudentsCaseManager(int studentId, String username) {
        CaseManager caseManager = this.caseManagerDao.getCurrentCaseManagerDetailByStudentId(studentId);
        return username.equalsIgnoreCase(caseManager.getCaseManagerUsername());
    }


}
