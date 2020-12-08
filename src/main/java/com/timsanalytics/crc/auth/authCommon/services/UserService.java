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
    private final com.timsanalytics.crc.auth.authCommon.dao.UserDao userDao;
    private final PlatformTransactionManager mySqlAuthTransactionManager;
    private final PrintObjectService printObjectService;
    private final UserRoleService userRoleService;

    @Autowired
    public UserService(UserDao userDao,
                       PlatformTransactionManager mySqlAuthTransactionManager,
                       PrintObjectService printObjectService,
                       UserRoleService userRoleService) {
        this.userDao = userDao;
        this.mySqlAuthTransactionManager = mySqlAuthTransactionManager;
        this.userRoleService = userRoleService;
        this.printObjectService = printObjectService;
    }

    public User createUser(User User, User loggedInUser) {
        this.logger.debug("UserService -> createUser: username=" + User.getUsername());
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = mySqlAuthTransactionManager.getTransaction(txDef);
        User user;
        try {
            // Create the new User.
            user = this.userDao.createUser(User, loggedInUser);
            this.printObjectService.PrintObject("UserService -> createUser: User", user);

            // Add the Roles to the new User.
            for (int i = 0; i < User.getRoles().size(); i++) {
                this.userRoleService.createUserRole(
                        user.getUserId(),
                        User.getRoles().get(i).getRoleId(),
                        User.getRoles().get(i).getDeleted(),
                        loggedInUser
                );
            }

            // If everything was successful, commit to the database.
            mySqlAuthTransactionManager.commit(txStatus);
            this.logger.debug("UserService -> createUser -> response: " + user);
        } catch (Exception e) {
            mySqlAuthTransactionManager.rollback(txStatus);
            logger.error("Error during creation: " + User.getUsername(), e);
            throw e;
        }
        return user;
    }

    public List<User> getUserList() {
        this.logger.debug("UserService -> getUserList");
        List<User> list = this.userDao.getUserList();
        return list;
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

    public User updateUser(User user, User loggedInUser) {
        this.logger.debug("UserService -> updateUser: username=" + user.getUsername());
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = mySqlAuthTransactionManager.getTransaction(txDef);
        User updatedUser;
        try {
            // Update the User.
            updatedUser = this.userDao.updateUser(user, loggedInUser);

            // Update the User's Roles
//            this.printObjectService.PrintObject("roles", user.getRoles());
            this.userRoleService.updateUserRoleBatch(user.getUserId(), user.getRoles());

//            if(user.getRoles() != null && user.getRoles().size() > 0) {
//                for (int i = 0; i < user.getRoles().size(); i++) {
//                    this.userRoleService.updateUserRole(
//                            updatedUser.getUserId(),
//                            user.getRoles().get(i).getRoleId(),
//                            user.getRoles().get(i).getStatus(),
//                            loggedInUser
//                    );
//                }
//            }

            // If everything was successful, commit to the database.
            mySqlAuthTransactionManager.commit(txStatus);
            this.logger.debug("UserService -> updateUser -> response: " + updatedUser);
        } catch (Exception e) {
            mySqlAuthTransactionManager.rollback(txStatus);
            logger.error("Error during update: " + user.getUsername(), e);
            throw e;
        }
        return updatedUser;
    }

    // DELETE
    public User deleteUser(User user, User loggedInUser) {
        this.logger.debug("UserService -> deleteUser: userId=" + user.getUserId());
        User deletedUser = this.userDao.deleteUser(user.getUserId());
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
            this.printObjectService.PrintObject("UserService -> updateMyProfile: User", item);

            // If everything was successful, commit to the database.
            mySqlAuthTransactionManager.commit(txStatus);
            this.logger.debug("UserService -> updateMyProfile -> response: " + item);
        } catch (Exception e) {
            mySqlAuthTransactionManager.rollback(txStatus);
            logger.error("Error during update: " + User.getUsername(), e);
            throw e;
        }
        return item; // Note: this User does not include the updates
    }

    // NON-CRUD SERVICES

    public User changePassword(User User, User loggedInUser) {
        this.logger.debug("UserService -> changePassword");
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = mySqlAuthTransactionManager.getTransaction(txDef);
        User item;
        try {
            // Update the User.
            item = this.userDao.changePassword(User, loggedInUser);
            this.printObjectService.PrintObject("UserService -> changePassword: User", item);

            // If everything was successful, commit to the database.
            mySqlAuthTransactionManager.commit(txStatus);
            this.logger.debug("UserService -> changePassword -> response: " + item);
        } catch (Exception e) {
            mySqlAuthTransactionManager.rollback(txStatus);
            logger.error("Error during update: " + User.getUsername(), e);
            throw e;
        }
        return item; // Note: this User does not include the updates
    }

    public Integer getUserGuidByUsername(String username) {
        this.logger.debug("UserService -> getUserGuidByUsername: username=" + username);
        return this.userDao.getUserIdByUsername(username);
    }

    public List<Role> getUserRoles(Integer userGuid) {
        this.logger.debug("UserService -> getUserRoles: userGuid=" + userGuid);
        return this.userDao.getUserRoles(userGuid);
    }

}
