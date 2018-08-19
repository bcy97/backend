package com.backend.service;

import com.backend.vo.AcStatisData;
import com.backend.vo.Cumulant;

import java.util.Date;
import java.util.List;

public interface CumulantStatisService {
    List<String> getUnitList();

    List<Cumulant> getDataByUnitName(String unitName);

    AcStatisData[] getDataByUnitNameAndTime(Date stime, Date etime, String unitName);
}
