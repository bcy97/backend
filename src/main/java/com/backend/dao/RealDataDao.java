package com.backend.dao;

import com.backend.vo.AcValue;
import com.backend.vo.AnValue;
import com.backend.vo.StValue;

public interface RealDataDao {
    Object[] getRealData(Integer[] ids, String companyId);

    AnValue[] getAnRealData(Integer[] ids, String companyId);

    StValue[] getStRealData(Integer[] ids, String companyId);

    AcValue[] getAcRealData(Integer[] ids, String companyId);
}
