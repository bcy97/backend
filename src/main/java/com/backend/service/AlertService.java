package com.backend.service;

import com.backend.vo.EventInfo;

import java.util.List;

public interface AlertService {

    /**
     * 获取重要警报，默认为20条
     *
     * @return 一个事件列表
     */
    List<EventInfo> getImportantAlert();
}
