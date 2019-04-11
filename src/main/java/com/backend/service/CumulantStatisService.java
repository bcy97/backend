package com.backend.service;

import com.backend.vo.AcStatisData;
import com.backend.vo.Cumulant;

import java.util.Date;
import java.util.List;

public interface CumulantStatisService {
    List<String> getUnitList(String companyId);

    List<Cumulant> getDataByUnitName(String unitName, String companyId);

    List<Cumulant> getCumulantDataByUnitNameAndTime(Date stime, Date etime,String unitName, String companyId);

 //   AcStatisData[] getDataByUnitNameAndTime(Date stime, Date etime, String unitName);
}
