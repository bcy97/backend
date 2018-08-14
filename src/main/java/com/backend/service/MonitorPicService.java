package com.backend.service;

import java.util.List;

public interface MonitorPicService {

    Object[] getRealData(int[] ids);

    /**
     * 新添加方法
     * @return
     */
    List<String> getPicList();

}
