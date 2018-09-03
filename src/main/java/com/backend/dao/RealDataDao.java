package com.backend.dao;

import com.backend.vo.AcValue;
import com.backend.vo.AnValue;
import com.backend.vo.StValue;

public interface RealDataDao {
    Object[] getRealData(Integer[] ids);

    AnValue[] getAnRealData(Integer[] ids);

    StValue[] getStRealData(Integer[] ids);

    AcValue[] getAcRealData(Integer[] ids);
}
