package com.backend.dao;

import com.backend.vo.EventInfo;

public interface EventInfoDao {
    /***
     * 最新事件信息;最近3个月内最新的20条的事件信息
     * */
    EventInfo[] getLatestEventInfo(String companyId);

    /***
     * 获取某段时间，某些点的事件信息
     * */
    EventInfo[] getEventInfoByTimeAndId(Integer[] ids, String begTime, String endTime, byte type, String companyId);

}
