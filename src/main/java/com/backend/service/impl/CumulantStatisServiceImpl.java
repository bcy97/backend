package com.backend.service.impl;

import com.backend.dao.StatisDataDao;
import com.backend.service.CumulantStatisService;
import com.backend.util.CfgData;
import com.backend.util.Utils;
import com.backend.vo.AcStatisData;
import com.backend.vo.Cumulant;
import com.backend.vo.UnitInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CumulantStatisServiceImpl implements CumulantStatisService {

    static Logger logger = Logger.getLogger("CumulantStatisServiceImpl");

    private StatisDataDao statisDataDao;
    private Utils utils;

    @Autowired
    public CumulantStatisServiceImpl(StatisDataDao statisDataDao, Utils utils) {
        this.statisDataDao = statisDataDao;
        this.utils = utils;
    }

    @Override
    public List<String> getUnitList() {
        CfgData cfgData = new CfgData();

        List<String> unitNameList = new ArrayList<>();
        for (UnitInfo ui : cfgData.getAllUnitInfo())
            unitNameList.add(ui.getName());

        return unitNameList;
    }

    @Override
    public List<Cumulant> getDataByUnitName(String unitName) {
        CfgData cfgData = new CfgData();

        Integer[] ids = utils.getAcIdsByUnitName(unitName);

        Calendar currTime = Calendar.getInstance();
        Calendar begTime = Calendar.getInstance();
        begTime.set(Calendar.HOUR_OF_DAY, 0);
        begTime.set(Calendar.MINUTE, 0);
        begTime.set(Calendar.SECOND, 0);
        begTime.set(Calendar.MILLISECOND, 0);

        AcStatisData[] todayStatisDatas = getDataByUnitNameAndTime(begTime.getTime(), currTime.getTime(), unitName);

        Calendar endTime = Calendar.getInstance();
        endTime.setTimeInMillis(begTime.getTimeInMillis());

        begTime.add(Calendar.DAY_OF_MONTH, -1);

        AcStatisData[] lastdayStatisDatas = getDataByUnitNameAndTime(begTime.getTime(), endTime.getTime(), unitName);

        begTime.set(Calendar.DAY_OF_MONTH, 0);

        AcStatisData[] thisMonthStatisDatas = getDataByUnitNameAndTime(begTime.getTime(), currTime.getTime(), unitName);

        endTime.setTimeInMillis(begTime.getTimeInMillis());

        begTime.add(Calendar.MONTH, -1);

        AcStatisData[] lastMonthStatisDatas = getDataByUnitNameAndTime(begTime.getTime(), endTime.getTime(), unitName);

        List<Cumulant> list = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            Cumulant cumulant = new Cumulant();
            cumulant.setId(ids[i]);
            cumulant.setName(cfgData.getAcO(ids[i]).getCname());
            cumulant.setToday(todayStatisDatas[i].getAccValue().getValue());
            cumulant.setLastday(lastdayStatisDatas[i].getAccValue().getValue());
            cumulant.setThisMonth(thisMonthStatisDatas[i].getAccValue().getValue());
            cumulant.setLastMonth(lastMonthStatisDatas[i].getAccValue().getValid());
            list.add(cumulant);
        }

        return null;
    }

    @Override
    public AcStatisData[] getDataByUnitNameAndTime(Date stime, Date etime, String unitName) {
        Integer[] ids = utils.getAcIdsByUnitName(unitName);
        return statisDataDao.getAcStatisData(ids, utils._DATE_FORMAT_.format(stime), utils._DATE_FORMAT_.format(etime));
    }
}
