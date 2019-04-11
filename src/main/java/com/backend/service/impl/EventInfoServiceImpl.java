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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class EventInfoServiceImpl implements EventInfoService {

    static Logger logger = Logger.getLogger("EventInfoServiceImpl");

    private EventInfoDao eventInfoDao;
    private Utils utils;
    private CfgData cfgData;

    @Autowired
    public EventInfoServiceImpl(EventInfoDao eventInfoDao, Utils utils, CfgData cfgData) {
        this.eventInfoDao = eventInfoDao;
        this.utils = utils;
        this.cfgData = cfgData;
    }

    /***
     * 根据时间段和点名获取 时间精确到5min
     * */
    @Override
    public EventInfo[] getEventByTimeAndPointName(Date stime, Date etime, String pointName, String companyId) {
        int id = -1;
        byte type = -1;

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

        return eventInfoDao.getEventInfoByTimeAndId(new Integer[]{id}, utils._DATE_FORMAT_.format(stime), utils._DATE_FORMAT_.format(etime), type, companyId);
    }

    @Override
    public EventInfo[] getEventByTimeAndUnitNames(Date stime, Date etime, List<String> unitnames,int type, String companyId) {
        List<Integer> ids = new ArrayList<Integer>();

        byte dataType = Constants.CC_EDT_STEPD;

        for (String unitName : unitnames) {
            if (1 == type) {
                ids.addAll(Arrays.asList(utils.getAnIdsByUnitName(unitName)));
                dataType = Constants.CC_EDT_ANEPD;
            } else {
                ids.addAll(Arrays.asList(utils.getStIdsByUnitName(unitName)));
                dataType = Constants.CC_EDT_STEPD;
            }
        }

        return eventInfoDao.getEventInfoByTimeAndId(ids.toArray(new Integer[ids.size()]), utils._DATE_FORMAT_.format(stime), utils._DATE_FORMAT_.format(etime), Constants.CC_EDT_STEPD, companyId);
    }

	@Override
	public EventInfo[] getStEventByEname(Date begTime, Date endTime, String ename, String companyId) {
		StO sto = cfgData.getStO(ename);
		if(sto != null) {
			return eventInfoDao.getEventInfoByTimeAndId(new Integer[]{sto.getId()}, utils._DATE_FORMAT_.format(begTime), utils._DATE_FORMAT_.format(endTime), Constants.CC_EDT_STEPD, companyId);
		}
		return null;
	}
}