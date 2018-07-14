package com.backend.service.impl;

import com.backend.service.UserService;
import com.backend.util.Constants;
import com.backend.util.SocketConnect;
import com.backend.vo.DataPacket;
import com.backend.vo.UserInfo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    static Logger logger = Logger.getLogger("UserService");

    @Override
    public UserInfo[] getAllUserInfo() {
        ByteBuffer bb = getUserInfo(Constants.CC_USERINFO, new byte[0]);
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
        String name = "";
        String pwd = "";
        String id = "";
        String depaType = "";
        String depaName = "";
        byte roleCount = 0;
        String[] roleIds = null;
        byte permissionType = 0;
        String permissionName = "";
        long controlType = 0;
        int size = bb.position();
        byte strLength = 0;
        byte[] datas;
        List<UserInfo> list = new ArrayList<UserInfo>();
        bb.flip();

        while (size > 0) {
            strLength = bb.get();
            size--;
            datas = new byte[strLength];
            bb.get(datas);
            size -= strLength;
            id = new String(datas, "UTF-8");

            strLength = bb.get();
            size--;
            datas = new byte[strLength];
            bb.get(datas);
            size -= strLength;
            name = new String(datas, "UTF-8");

            strLength = bb.get();
            size--;
            datas = new byte[strLength];
            bb.get(datas);
            size -= strLength;
            pwd = new String(datas, "UTF-8");

            strLength = bb.get();
            size--;
            datas = new byte[strLength];
            bb.get(datas);
            size -= strLength;
            depaType = new String(datas, "UTF-8");

            strLength = bb.get();
            size--;
            datas = new byte[strLength];
            bb.get(datas);
            size -= strLength;
            depaName = new String(datas, "UTF-8");

            roleCount = bb.get();
            size--;
            roleIds = new String[roleCount];
            for (byte i = 0; i < roleCount; i++) {
                strLength = bb.get();
                size--;
                datas = new byte[strLength];
                bb.get(datas);
                size -= strLength;
                roleIds[i] = new String(datas, "UTF-8");
            }

            permissionType = bb.get();
            size--;

            strLength = bb.get();
            size--;
            datas = new byte[strLength];
            bb.get(datas);
            size -= strLength;
            permissionName = new String(datas, "UTF-8");

            controlType = bb.getLong();

            list.add(new UserInfo(name, pwd, depaName, id, depaType));

        }

        return list.toArray(new UserInfo[list.size()]);
    }

    private ByteBuffer getUserInfo(byte type, byte[] datas) {
        ByteBuffer bb = null;
        DataPacket dp = new DataPacket(Constants.CC_USERINFO, datas);
        byte[] bDatas = dp.serialize();

        SocketConnect.getData(bb, dp, bDatas, logger);

        return bb;
    }
}
