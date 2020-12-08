package com.timsanalytics.crc.auth.authCommon.services;

import com.timsanalytics.crc.auth.authCommon.beans.Role;
import com.timsanalytics.crc.auth.authCommon.dao.RoleDao;
import com.timsanalytics.crc.main.beans.types.InteractionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleDao roleDao;

    @Autowired
    public RoleService(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public List<Role> getRoleList() {
        return this.roleDao.getRoleList();
    }

    public List<Role> getRoleListByUserId(Integer roleId) {
        return this.roleDao.getRoleListByUserId(roleId);
    }
}
