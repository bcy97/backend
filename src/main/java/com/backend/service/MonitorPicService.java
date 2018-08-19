package com.backend.service;

import java.util.List;

public interface MonitorPicService {

    Object[] getRealData(Integer[] ids);

    /**
     * 新添加方法
     * @return List<String> 返回监控图列表
     */
    List<String> getPicList();

}
