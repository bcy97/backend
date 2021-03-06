package com.backend.service.impl;

import com.backend.dao.HistoryDataDao;
import com.backend.dao.RealDataDao;
import com.backend.service.LineService;
import com.backend.util.CfgData;
import com.backend.util.Utils;
import com.backend.vo.AnValue;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LineServiceImpl implements LineService {

    static Logger logger = Logger.getLogger("LineServiceImpl");

    private RealDataDao realDataDao;
    private HistoryDataDao historyDataDao;
    private Utils utils;
    private CfgData cfgData;

    @Autowired
    public LineServiceImpl(RealDataDao realDataDao, HistoryDataDao historyDataDao, Utils utils, CfgData cfgData) {
        this.realDataDao = realDataDao;
        this.historyDataDao = historyDataDao;
        this.utils = utils;
        this.cfgData = cfgData;
    }

    /**
     * 新增方法：根据曲线图名和时间获取实时数据
     *
     * @param ptNames
     * @return 点名和数据的Map
     */
    @Override
    public Map<String, Float> getRealLineData(String[] ptNames, String companyId) {
        Integer[] ids = new Integer[ptNames.length];

        for (int i = 0; i < ids.length; i++)
            ids[i] = cfgData.getAnID(ptNames[i], companyId);

        AnValue[] anValues = realDataDao.getAnRealData(ids, companyId);

        Map<String, Float> rtnMap = new HashMap<>();
        for (int i = 0; i < anValues.length; i++)
            rtnMap.put(ptNames[i], anValues[i].getValue());

        return rtnMap;
    }

    /**
     * 新增方法：根据曲线图名和时间获取历史数据
     *
     * @param stime
     * @param etime
     * @param ptNames
     * @return 点名和数据的Map
     */
    @Override
    public Map<String, Float[]> getHistoryLineData(Date stime, Date etime, String[] ptNames, String companyId) {
        Integer[] ids = utils.anPtNamesToIds(ptNames, companyId);

        Calendar begCal = Calendar.getInstance();
        begCal.setTime(stime);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(etime);
        // 获取某段时间的5分钟点
        List<String> list = utils.get5MinPoint(begCal, endCal);
        // 数组结构 AnValue[id][时间] 即 第1个id某段时间的数据、第2个id某段时间的数据、第3个id某段时间的数据......第n个id某段时间的数据
        AnValue[] anValues = historyDataDao.getAn5MinHistoryData(utils._DATE_FORMAT_.format(stime), utils._DATE_FORMAT_.format(etime), ids, list.size(), companyId);

        Map<String, Float[]> map = new HashMap<>();
        for (int i = 0; i < ptNames.length; i++) {
            Float[] datas = new Float[list.size()];
            for (int j = 0; j < datas.length; j++)
                datas[j] = anValues[i * datas.length + j].getValue();
            map.put(ptNames[i], datas);
        }

        return map;
    }
}
