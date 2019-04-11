package com.backend.service.impl;

import com.backend.dao.UserDao;
import com.backend.service.UserService;
import com.backend.vo.UserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    static Logger logger = Logger.getLogger("UserServiceImpl");

    private UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean login(String username, String password, String companyId) {

        UserInfo[] userInfos = userDao.getAllUserInfo(companyId);
        if (null == userInfos)
            return false;
        String temp = "count:";
        temp = null == userInfos ? "null" : Integer.toString(userInfos.length);
        System.out.println(temp);
        
        for (int i = 0; i < userInfos.length; i++) {
            if (userInfos[i].getName().equals(username) && userInfos[i].getPwd().equals(password)) {
                return true;
            }
        }

        return false;
    }
}
