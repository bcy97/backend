package com.backend.dao.impl;

import com.backend.dao.UserDao;
import com.backend.util.Constants;
import com.backend.util.SocketConnect;
import com.backend.vo.UserInfo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    static Logger logger = Logger.getLogger("UserDaoImpl");

    @Override
    public UserInfo[] getAllUserInfo(String companyId) {
        ByteBuffer bb = getUserInfo(new byte[0], companyId);
        if (null == bb)
            return null;
        try {
            return parseUserInfo(bb);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    private UserInfo[] parseUserInfo(ByteBuffer bb)
            throws UnsupportedEncodingException {
        String name;
        String pwd;
        String id;
        String depaType;
        String depaName;
        byte roleCount;
        String[] roleIds;
        byte permissionType = 0;
        String permissionName = "";
        long controlType = 0;
        int size = bb.position();
        byte strLength;
        byte[] datas;
        List<UserInfo> list = new ArrayList<>();
        bb.flip();

        if(0 == size)
            System.out.println("size is zero!");

        while (size > 0) {
//            strLength = bb.get();
//            size--;
//            datas = new byte[strLength];
//            bb.get(datas);
//            size -= strLength;
//            id = new String(datas, "UTF-8"); // id

            strLength = bb.get();
            size--;
            datas = new byte[strLength];
            bb.get(datas);
            size -= strLength;
            name = new String(datas, "UTF-8");// 用户名

            strLength = bb.get();
            size--;
            datas = new byte[strLength];
            bb.get(datas);
            size -= strLength;
            pwd = new String(datas, "UTF-8");// 密码

//            strLength = bb.get();
//            size--;
//            datas = new byte[strLength];
//            bb.get(datas);
//            size -= strLength;
//            depaType = new String(datas, "UTF-8");// 部门id

//            bb.getInt();// 部门类别

//            strLength = bb.get();
//            size--;
//            datas = new byte[strLength];
//            bb.get(datas);
//            size -= strLength;
//            depaName = new String(datas, "UTF-8");// 部门编号


            strLength = bb.get();
            size--;
            datas = new byte[strLength];
            bb.get(datas);
            size -= strLength;
            depaName = new String(datas, "UTF-8");// 部门名称

            bb.get();// 权限类别
            size--;

            bb.getLong();
            bb.getLong();// 控制权限
            size -= 16;
//
//            roleCount = bb.get();
//            size--;
//            roleIds = new String[roleCount];
//            for (byte i = 0; i < roleCount; i++) {
//                strLength = bb.get();
//                size--;
//                datas = new byte[strLength];
//                bb.get(datas);
//                size -= strLength;
//                roleIds[i] = new String(datas, "UTF-8");
//            }
//
//            permissionType = bb.get();
//            size--;
//
//            strLength = bb.get();
//            size--;
//            datas = new byte[strLength];
//            bb.get(datas);
//            size -= strLength;
//            permissionName = new String(datas, "UTF-8");
//
//            controlType = bb.getLong();

            list.add(new UserInfo(name, pwd, depaName));

        }

        return list.toArray(new UserInfo[list.size()]);
    }

    private ByteBuffer getUserInfo(byte[] datas, String companyId) {
        ByteBuffer bb;

        bb = SocketConnect.getData(datas,Constants.CC_USERINFO,logger,true, companyId);

        return bb;

    }
}
