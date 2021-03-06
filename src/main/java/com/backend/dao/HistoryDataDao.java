package com.backend.dao;

import com.backend.vo.AcValue;
import com.backend.vo.AnValue;
import com.backend.vo.StValue;
import org.apache.log4j.Logger;

public interface HistoryDataDao {


    AnValue[] getAnHistoryData(String begTime, String endTime, Integer[] ids, int count, String companyId);

    AnValue[] getAn5MinHistoryData(String begTime, String endTime, Integer[] ids, int count, String companyId);

    StValue[] getStHistoryData(String begTime, String endTime, Integer[] ids, int count, String companyId);

    AcValue[] getAcHistoryData(String begTime, String endTime, Integer[] ids, int count, String companyId);
}
