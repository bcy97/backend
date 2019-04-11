package com.backend.dao;

import com.backend.vo.UserInfo;

public interface UserDao {

    UserInfo[] getAllUserInfo(String companyId);

}
