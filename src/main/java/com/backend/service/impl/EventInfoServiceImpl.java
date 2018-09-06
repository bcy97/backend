package com.backend.service.impl;

import com.backend.dao.EventInfoDao;
import com.backend.service.EventInfoService;
import com.backend.util.CfgData;
import com.backend.util.Constants;
import com.backend.util.Utils;
import com.backend.vo.AnO;
import com.backend.vo.EventInfo;
import com.backend.vo.StO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EventInfoServiceImpl implements EventInfoService {

    static Logger logger = Logger.getLogger("EventInfoServiceImpl");

    private EventInfoDao eventInfoDao;
    private Utils utils;

    @Autowired
    public EventInfoServiceImpl(EventInfoDao eventInfoDao, Utils utils) {
        this.eventInfoDao = eventInfoDao;
        this.utils = utils;
    }

    /***
     * 根据时间段和点名获取 时间精确到5min
     * */
    @Override
    public EventInfo[] getEventByTimeAndPointName(Date stime, Date etime, String pointName) {
        int id = -1;
        byte type = -1;

        CfgData cfgData = new CfgData();
        AnO ano = cfgData.getAnO(pointName);
        if (null != ano) {
            id = ano.getId();
            type = Constants.CC_EDT_ANEPD;
        } else {
            StO sto = cfgData.getStO(pointName);
            if (null != sto) {
                id = sto.getId();
                type = Constants.CC_EDT_STEPD;
            }
        }
        if (-1 == id)
            return null;

        return eventInfoDao.getEventInfoByTimeAndId(new Integer[]{id}, utils._DATE_FORMAT_.format(stime), utils._DATE_FORMAT_.format(etime), type);
    }
}