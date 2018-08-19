package com.backend.service.impl;

import com.backend.dao.EventInfoDao;
import com.backend.service.AlertService;
import com.backend.util.CfgData;
import com.backend.util.Constants;
import com.backend.util.Utils;
import com.backend.vo.EventInfo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class AlertServiceImpl implements AlertService {

    static Logger logger = Logger.getLogger("AlertServiceImpl");

    private EventInfoDao eventInfoDao;

    public AlertServiceImpl(){
    }

    public AlertServiceImpl(EventInfoDao eventInfoDao){
        this.eventInfoDao = eventInfoDao;
    }

    /***
     * 获取最新报警信息，即最近3个月内的前20条的遥信EPD信息
     * */
    @Override
    public EventInfo[] getImportantAlert() {
        Calendar begTime = Calendar.getInstance();
        begTime.add(Calendar.MONTH,-3);

        Calendar endTime = Calendar.getInstance();

        CfgData cfgData = new CfgData();

        EventInfo[] infos = eventInfoDao.getEventInfoByTimeAndId(cfgData.getAllStId(),Utils._DATE_FORMAT_.format(begTime.getTime()),Utils._DATE_FORMAT_.format(endTime.getTime()),Constants.CC_EDT_STEPD);

        if(infos.length <= 20)
            return infos;

        EventInfo[] rtnData = new EventInfo[20];
        for(int i = 0; i < 20; i++)
            infos[i] = infos[infos.length - 1 - i];

        return rtnData;
    }
}
