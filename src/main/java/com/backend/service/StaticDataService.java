package com.backend.service;

import com.backend.vo.AcStatisData;
import com.backend.vo.AnStatisData;
import com.backend.vo.StStatisData;

import java.text.ParseException;

public interface StaticDataService {
    AnStatisData[] getAnStatisData(int[] ids, String begTime,
                                   String endTime) throws ParseException;

    StStatisData[] getStStatisData(int[] ids, String begTime,
                                   String endTime) throws ParseException;

    AcStatisData[] getAcStatisData(int[] ids, String begTime,
                                   String endTime) throws ParseException;
}
