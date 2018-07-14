package com.backend.service;

import com.backend.vo.AcValue;
import com.backend.vo.AnValue;
import com.backend.vo.StValue;

import java.text.ParseException;

public interface HistoryDataService {
    AnValue[] getHistoryAnHourData(int[] ids, String begTime,
                                   String endTime) throws ParseException;

    AnValue[] getHistoryAn5MinData(int[] ids, String begTime,
                                   String endTime) throws ParseException;

    StValue[] getHistoryStHourData(int[] ids, String begTime,
                                   String endTime) throws ParseException;

    AcValue[] getHistoryAcHourData(int[] ids, String begTime,
                                   String endTime) throws ParseException;
}
