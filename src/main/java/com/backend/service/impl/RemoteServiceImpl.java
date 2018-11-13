package com.backend.service.impl;

import com.backend.dao.RemoteDao;
import com.backend.service.RemoteService;
import com.backend.util.CfgData;
import com.backend.util.Constants;
import com.backend.vo.StO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RemoteServiceImpl implements RemoteService {

    private CfgData cfgData;
    private RemoteDao remoteDao;

    @Autowired
    public RemoteServiceImpl(CfgData cfgData,RemoteDao remoteDao){
        this.cfgData = cfgData;
        this.remoteDao = remoteDao;
    }

    @Override
    public void remoteControl(String[] ptNames,Byte state) {
        // 下发遥控
        for(String ptName : ptNames){
            StO sto = cfgData.getStO(ptName);
            // 通过点名找不到该点或该点的类型不是灯光，则不对该点进行遥控处理
            if(null == sto || Constants.LIGHT_TYPE != sto.getType())
                continue;
            // 下发状态非0非1也跳过
            if(0 != state && 1 != state)
                continue;
            if(state == 0)
                remoteDao.remoteControl(ptName,sto.getSwidef());
            else// 下发分命令
                remoteDao.remoteControl(ptName,0 == sto.getSwidef() ? (byte)1 :(byte)0);

        }
    }
}
