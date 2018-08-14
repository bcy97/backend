package com.backend.service.impl;

import com.backend.service.CumulantStatisService;
import com.backend.vo.Cumulant;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CumulantStatisServiceImpl implements CumulantStatisService {

    static Logger logger = Logger.getLogger("CumulantStatisServiceImpl");

    @Override
    public List<String> getUnitList() {
        return null;
    }

    @Override
    public List<Cumulant> getDataByUnitName(String unitName) {
        return null;
    }

    @Override
    public List<Cumulant> getDataByUnitNameAndTime(Date stime, Date etime, String unitName) {
        return null;
    }
}
