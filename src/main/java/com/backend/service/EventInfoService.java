package com.backend.service;

import com.backend.vo.EventInfo;

import java.util.Date;
import java.util.List;

public interface EventInfoService {

    /**
     * 根据时间段和点名获取 时间精确到5min
     *
     * @param stime
     * @param etime
     * @param pointName
     * @return
     */
    EventInfo[] getEventByTimeAndPointName(Date stime, Date etime, String pointName, String companyId);

    /**
     * 根据时间段和单元名列表获取 时间精确到5min
     *
     * @param stime
     * @param etime
     * @param unitnames
     * @return
     */
    EventInfo[] getEventByTimeAndUnitNames(Date stime, Date etime, List<String> unitnames,int type, String companyId);

    EventInfo[] getStEventByEname(Date begTime, Date endTime, String ename, String companyId);
}
