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
     * @param ptNames 曲线图名
     * @return 时间和数据的Map
     */
    Map<String, Float> getRealLineData(String[] ptNames);

    /**
     * 新增方法：根据曲线图名和时间获取历史数据
     *
     * @param stime 开始时间
     * @param etime 结束时间
     * @param ptNames 曲线图名
     * @return 时间和数据的Map
     */
    Map<String, Float[]> getHistoryLineData(Date stime, Date etime, String[] ptNames);


}
