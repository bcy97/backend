package com.backend.service;

import java.util.Date;
import java.util.Map;

/**
 * 获取曲线图数据的service
 */
public interface LineService {

    /**
     * 新增方法：根据曲线图名和时间获取实时数据
     *
     * @param picName
     * @return 时间和数据的Map
     */
    Map<Date, Float> getRealLineData(String picName);

    /**
     * 新增方法：根据曲线图名和时间获取历史数据
     *
     * @param stime
     * @param etime
     * @param picName
     * @return 时间和数据的Map
     */
    Map<Date, Float> getHistoryLineData(Date stime, Date etime, String picName);


}
