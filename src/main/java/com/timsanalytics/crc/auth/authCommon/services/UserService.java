package com.timsanalytics.crc.auth.authCommon.services;

import com.timsanalytics.crc.auth.authCommon.beans.Role;
import com.timsanalytics.crc.auth.authCommon.beans.User;
import com.timsanalytics.crc.auth.authCommon.dao.UserDao;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.utils.PrintObjectService;
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
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final UserDao userDao;
    private final PlatformTransactionManager mySqlAuthTransactionManager;
    private final UserRoleService userRoleService;
    private final PrintObjectService printObjectService;

    @Autowired
    public UserService(UserDao userDao,
                       PlatformTransactionManager mySqlAuthTransactionManager,
                       UserRoleService userRoleService,
                       PrintObjectService printObjectService) {
        this.userDao = userDao;
        this.mySqlAuthTransactionManager = mySqlAuthTransactionManager;
        this.userRoleService = userRoleService;
        this.printObjectService = printObjectService;
    }

    public User createUser(User user, User loggedInUser) {
        this.logger.debug("UserService -> createUser: username=" + user.getUserUsername());
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = mySqlAuthTransactionManager.getTransaction(txDef);
        User createdUser = null;
        try {
            // Create the new User.
            createdUser = this.userDao.createUser(user, loggedInUser);

            // Update the User's Roles
            this.userRoleService.updateUserRoleBatch(createdUser.getUserId(), user.getRoles());

            // If everything was successful, commit to the database.
            mySqlAuthTransactionManager.commit(txStatus);
            this.logger.debug("UserService -> createUser -> response: " + createdUser);
        } catch (Exception e) {
            mySqlAuthTransactionManager.rollback(txStatus);
            logger.error("Error during creation: " + user.getUserUsername());
        }
        return user;
    }

    public List<User> getUserList() {
        return this.userDao.getUserList();
    }

    public ServerSidePaginationResponse<User> getUserList_SSP(ServerSidePaginationRequest<User> serverSidePaginationRequest) {
        ServerSidePaginationResponse<User> serverSidePaginationResponse = new ServerSidePaginationResponse<>();
        serverSidePaginationResponse.setServerSidePaginationRequest(serverSidePaginationRequest);
        List<User> userList = this.userDao.getUserList_SSP(serverSidePaginationRequest);
        serverSidePaginationResponse.setData(userList);
        serverSidePaginationResponse.setLoadedRecords(userList.size());
        serverSidePaginationResponse.setTotalRecords(this.userDao.getUserList_SSP_TotalRecords(serverSidePaginationRequest));
        return serverSidePaginationResponse;
    }

    public User getUserDetail(Integer userId) {
        this.logger.debug("UserService -> getUserDetail: userId=" + userId);
        return this.userDao.getUserDetail(userId);
    }

    public User getUserDetailByUsername(String username) {
        this.logger.debug("UserService -> getUserDetailByUsername: username=" + username);
        return this.userDao.getUserDetailByUsername(username);
    }

    public User updateUser(User user, User loggedInUser) {
        this.logger.debug("UserService -> updateUser: username=" + user.getUserUsername());
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = mySqlAuthTransactionManager.getTransaction(txDef);
        User updatedUser = null;
        try {
            // Update the User.
            updatedUser = this.userDao.updateUser(user, loggedInUser);

            // Update the User's Roles
            this.userRoleService.updateUserRoleBatch(user.getUserId(), user.getRoles());

            // If everything was successful, commit to the database.
            mySqlAuthTransactionManager.commit(txStatus);
            this.logger.debug("UserService -> updateUser -> response: " + updatedUser);
        } catch (Exception e) {
            mySqlAuthTransactionManager.rollback(txStatus);
            logger.error("Error during update: " + user.getUserUsername(), e);
        }
        return updatedUser;
    }

    // DELETE
    public User deleteUser(Integer userId, User loggedInUser) {
        this.logger.debug("UserService -> deleteUser: userId=" + userId);
        User deletedUser = this.userDao.deleteUser(userId);
        this.logger.debug("UserService -> deleteUser: deletedUser=" + deletedUser.getUserId());
        return deletedUser;
    }

    // RELATED CRUD SERVICES

    public User disableUser(Integer userGuid) {
        this.logger.debug("UserService -> disableUser: userGuid=" + userGuid);
        return this.userDao.disableUser(userGuid);
    }

    public User enableUser(Integer userGuid) {
        this.logger.debug("UserService -> enableUser: userGuid=" + userGuid);
        return this.userDao.enableUser(userGuid);
    }

    public User updateMyProfile(User User, User loggedInUser) {
        this.logger.debug("UserService -> updateMyProfile: userGuid=" + loggedInUser.getUserId());
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = mySqlAuthTransactionManager.getTransaction(txDef);
        User item;
        try {
            // Update the User.
            item = this.userDao.updateMyProfile(User, loggedInUser);
            // If everything was successful, commit to the database.
            mySqlAuthTransactionManager.commit(txStatus);
            this.logger.debug("UserService -> updateMyProfile -> response: " + item);
        } catch (Exception e) {
            mySqlAuthTransactionManager.rollback(txStatus);
            logger.error("Error during update: " + User.getUserUsername(), e);
            throw e;
        }
        return item; // Note: this User does not include the updates
    }

    // NON-CRUD SERVICES

    public User updatePassword(User User, User loggedInUser) {
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = mySqlAuthTransactionManager.getTransaction(txDef);
        User item;
        try {
            item = this.userDao.updatePassword(User, loggedInUser);
            // If everything was successful, commit to the database.
            mySqlAuthTransactionManager.commit(txStatus);
        } catch (Exception e) {
            mySqlAuthTransactionManager.rollback(txStatus);
            logger.error("Error during update: " + User.getUserUsername(), e);
            throw e;
        }
        return item; // Note: this User does not include the updates
    }

    public User resetPassword(User User, User loggedInUser) {
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = mySqlAuthTransactionManager.getTransaction(txDef);
        User item;
        try {
            item = this.userDao.resetPassword(User, loggedInUser);
            // If everything was successful, commit to the database.
            mySqlAuthTransactionManager.commit(txStatus);
        } catch (Exception e) {
            mySqlAuthTransactionManager.rollback(txStatus);
            logger.error("Error during update: " + User.getUserUsername(), e);
            throw e;
        }
        return item; // Note: this User does not include the updates
    }

    public void updateScreenResolution(String resolution, User loggedInUser) {
        this.userDao.updateScreenResolution(resolution, loggedInUser);
    }

    public Integer getUserIdByUsername(String username) {
        this.logger.debug("UserService -> getUserIdByUsername: username=" + username);
        return this.userDao.getUserIdByUsername(username);
    }

    public List<Role> getUserRoles(Integer userGuid) {
        this.logger.debug("UserService -> getUserRoles: userGuid=" + userGuid);
        return this.userDao.getUserRoles(userGuid);
    }

}
