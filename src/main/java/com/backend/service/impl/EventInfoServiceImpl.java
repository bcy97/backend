package com.backend.service.impl;

import com.backend.service.EventInfoService;
import com.backend.vo.EventInfo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EventInfoServiceImpl implements EventInfoService {

    static Logger logger = Logger.getLogger("EventInfoServiceImpl");

    @Override
    public List<EventInfo> getEventByTimeAndPointName(Date stime, Date etime, String pointName) {
        return null;
    }
}