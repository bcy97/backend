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
import org.apache.tomcat.util.bcel.Const;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EventInfoServiceImpl implements EventInfoService {

    static Logger logger = Logger.getLogger("EventInfoServiceImpl");
    private EventInfoDao eventInfoDao = null;

    public EventInfoServiceImpl(){}

    public EventInfoServiceImpl(EventInfoDao eventInfoDao){
        this.eventInfoDao = eventInfoDao;
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
        if(null != ano){
            id  = ano.getId();
            type = Constants.CC_EDT_ANEPD;
        }else{
            StO sto = cfgData.getStO(pointName);
            if(null != sto){
                id = sto.getId();
                type = Constants.CC_EDT_STEPD;
            }
        }
        if(-1 == id)
            return null;

        return eventInfoDao.getEventInfoByTimeAndId(new Integer[]{id}, Utils._DATE_FORMAT_.format(stime),Utils._DATE_FORMAT_.format(etime),type);
    }
}