package com.backend.service.impl;

import com.backend.service.LineService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class LineServiceImpl implements LineService {

    static Logger logger = Logger.getLogger("LineServiceImpl");

    @Override
    public Map<Date, Float> getRealLineData(String picName) {
        return null;
    }

    @Override
    public Map<Date, Float> getHistoryLineData(Date stime, Date etime, String picName) {
        return null;
    }
}
