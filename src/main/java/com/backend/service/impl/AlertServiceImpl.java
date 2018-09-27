package com.backend.service.impl;

import com.backend.dao.EventInfoDao;
import com.backend.service.AlertService;
import com.backend.util.CfgData;
import com.backend.util.Constants;
import com.backend.util.Utils;
import com.backend.vo.EventInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class AlertServiceImpl implements AlertService {

    static Logger logger = Logger.getLogger("AlertServiceImpl");

    private EventInfoDao eventInfoDao;
    private Utils utils;
    private CfgData cfgData;

    @Autowired
    public AlertServiceImpl(EventInfoDao eventInfoDao, Utils utils,CfgData cfgData) {
        this.eventInfoDao = eventInfoDao;
        this.utils = utils;
        this.cfgData = cfgData;
    }

    /***
     * 获取最新报警信息，即最近3个月内的前20条的遥信EPD信息
     * */
    @Override
    public EventInfo[] getImportantAlert() {
        Calendar begTime = Calendar.getInstance();
        begTime.add(Calendar.MONTH, -3);

        Calendar endTime = Calendar.getInstance();

        EventInfo[] infos = eventInfoDao.getEventInfoByTimeAndId(cfgData.getAllStId(), utils._DATE_FORMAT_.format(begTime.getTime()), utils._DATE_FORMAT_.format(endTime.getTime()), Constants.CC_EDT_STEPD);

        if (infos.length <= 20)
            return infos;

        EventInfo[] rtnData = new EventInfo[20];
        for (int i = 0; i < 20; i++)
            rtnData[i] = infos[i];

        return rtnData;
    }
}
