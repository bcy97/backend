package com.backend.service.impl;

import com.backend.service.RealDataService;
import com.backend.vo.AnValue;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RealDataServiceImpl implements RealDataService {

    static Logger logger = Logger.getLogger("RealDataServiceImpl");

    @Override
    public Map<String, AnValue> getRealData(String unitName) {
        return null;
    }

}
