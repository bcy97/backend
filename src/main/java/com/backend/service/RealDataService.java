package com.backend.service;

import com.backend.vo.AnValue;

import java.util.Map;

public interface RealDataService {

    /**
     * 根据单元名获取数据
     *
     * @param unitName 单元名
     * @return Map<String, AnValue> key为点名，value为AnValue
     */
    Map<String, AnValue> getRealData(String unitName, String companyId);

	Object[] getRealDataByEnames(String[] enames, String companyId);
}
