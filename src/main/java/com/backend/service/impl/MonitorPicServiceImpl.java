package com.backend.service.impl;

import com.backend.dao.RealDataDao;
import com.backend.service.MonitorPicService;
import com.backend.util.CfgData;
import com.backend.util.Constants;
import com.backend.util.SocketConnect;
import com.backend.util.Utils;
import com.backend.vo.AcValue;
import com.backend.vo.AnValue;
import com.backend.vo.DataPacket;
import com.backend.vo.StValue;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

@Service
public class MonitorPicServiceImpl implements MonitorPicService {

    static Logger logger = Logger.getLogger("MonitorPicServiceImpl");

    private RealDataDao realDataDao;

    public MonitorPicServiceImpl(){}

    public MonitorPicServiceImpl(RealDataDao realDataDao){
        this.realDataDao = realDataDao;
    }

    @Override
    public Object[] getRealData(Integer[] ids) {
        return realDataDao.getRealData(ids);
    }

    @Override
    public List<String> getPicList() {
        return null;
    }



}
