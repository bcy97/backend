package com.backend.service.impl;

import com.backend.service.AlertService;
import com.backend.vo.EventInfo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertServiceImpl implements AlertService {

    static Logger logger = Logger.getLogger("AlertServiceImpl");

    @Override
    public List<EventInfo> getImportantAlert() {
        return null;
    }
}
