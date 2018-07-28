package com.backend.service.impl;

import com.backend.dao.UserDao;
import com.backend.service.UserService;
import com.backend.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean login(String username, String password) {

        UserInfo[] userInfos = userDao.getAllUserInfo();

        for (int i = 0; i < userInfos.length; i++) {
            if (userInfos[i].getId().equals(username) && userInfos[i].getPwd().equals(password)) {
                return true;
            }
        }

        return false;
    }
}
