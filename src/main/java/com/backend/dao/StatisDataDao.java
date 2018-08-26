package com.backend.dao;

import com.backend.vo.AcStatisData;
import com.backend.vo.AnStatisData;
import com.backend.vo.StStatisData;

public interface StatisDataDao {

    AcStatisData[] getAcStatisData(Integer[] ids, String begTime, String endTime);

    AnStatisData[] getAnStaticData(Integer[] ids, String begTime, String endTime);
}
