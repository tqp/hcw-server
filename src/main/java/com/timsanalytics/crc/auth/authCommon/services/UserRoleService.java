package com.timsanalytics.crc.auth.authCommon.services;

import com.timsanalytics.crc.auth.authCommon.beans.Role;
import com.timsanalytics.crc.auth.authCommon.beans.User;
import com.timsanalytics.crc.auth.authCommon.beans.UserRole;
import com.timsanalytics.crc.auth.authCommon.dao.UserRoleDao;
import com.timsanalytics.crc.utils.GenerateUuidService;
import com.timsanalytics.crc.utils.PrintObjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRoleService {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final UserRoleDao userRoleDao;

    @Autowired
    public UserRoleService(UserRoleDao userRoleDao) {
        this.userRoleDao = userRoleDao;
    }

    public Integer createUserRole(Integer userGuid, Integer roleGuid, Integer deleted, User loggedInUser) {
        this.logger.debug("UserRoleService -> createUserRole");
        Integer item = this.userRoleDao.createUserRole(userGuid, roleGuid, deleted, loggedInUser);
        this.logger.debug("UserRoleService -> createUserRole -> response: " + item);
        return item;
    }

    public String updateUserRole(Integer userId, Integer roleId, String status, User loggedInUser) {
        this.logger.debug("UserRoleService -> updateUserRole");
        String item = this.userRoleDao.updateUserRole(userId, roleId, status, loggedInUser);
        this.logger.debug("UserRoleService -> updateUserRole -> response: " + item);
        return item;
    }

    public void updateUserRoleBatch(Integer userId, List<Role> rolesToUpdate) {
        // Because MySQL doesn't have a MERGE query like Oracle, we are forced to use MySQL's
        // ON DUPLICATE KEY UPDATE query. To make this work for us, we first have to get the key from the
        // association table, if one exists. If the key already exists in the table, the query will update
        // it with a status. If it doesn't exist, the query will create it.
        List<UserRole> userRoleList = this.createUserRoleListToAddRoleToUser(userId, rolesToUpdate);

        try {
            List<UserRole> newItems = userRoleList.stream()
                    .filter(item -> item.getUserRoleId() == null)
                    .collect(Collectors.toList());
            if (newItems.size() > 0) {
                int[] newRecordsUpdated = this.userRoleDao.insertUserRoleBatch(newItems);
            }
        } catch (Exception e) {
            throw e;
        }

        try {
            List<UserRole> existingItems = userRoleList.stream()
                    .filter(item -> item.getUserRoleId() != null)
                    .collect(Collectors.toList());
            if (existingItems.size() > 0) {
                int[] existingRecordsUpdated = this.userRoleDao.updateUserRoleBatch(existingItems);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private List<UserRole> createUserRoleListToAddRoleToUser(Integer userId, List<Role> rolesToUpdate) {
        return rolesToUpdate.stream()
                .map(item -> {
                    UserRole existingUserRole = this.userRoleDao.getUserRoleByUserIdAndRoleId(userId, item.getRoleId());
                    UserRole userRole = new UserRole();
                    userRole.setUserRoleId(existingUserRole != null ? existingUserRole.getUserRoleId() : null);
                    userRole.setUserId(userId);
                    userRole.setRoleId(item.getRoleId());
                    userRole.setStatus(item.getStatus());
                    return userRole;
                })
                .collect(Collectors.toList());
    }
}
