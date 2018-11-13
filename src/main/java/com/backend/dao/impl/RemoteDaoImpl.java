package com.backend.dao.impl;

import com.backend.dao.RemoteDao;
import com.backend.util.Constants;
import com.backend.util.SocketConnect;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Repository
public class RemoteDaoImpl implements RemoteDao {

    static Logger logger = Logger.getLogger("RemoteDaoImpl");

    @Autowired
    public RemoteDaoImpl(){

    }

    @Override
    public void remoteControl(String ptName,byte state) {
        byte[] datas = getRemoteData(ptName,state);
        if(null == datas)
            return;
        SocketConnect.getData(datas, Constants.CC_REMOTECONTROL, logger,false);
    }

    /***
     * 通过点名和状态下发报文
     * */
    private byte[] getRemoteData(String ptName,byte state) {
        try {
            ByteBuffer bb = ByteBuffer.allocate(256).order(ByteOrder.LITTLE_ENDIAN);

            byte[] datas = ptName.getBytes("UTF-8");
            bb.put((byte)datas.length);
            bb.put(datas);
            bb.putShort(state);
            bb.putShort((short)0);

            datas = new byte[bb.position()];
            System.arraycopy(bb.array(), 0, datas, 0, datas.length);

            return datas;
        }catch (UnsupportedEncodingException e){
            return null;
        }
    }
}
