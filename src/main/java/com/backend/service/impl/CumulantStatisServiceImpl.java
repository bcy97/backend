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
    private CfgData cfgData;

    @Autowired
    public CumulantStatisServiceImpl(StatisDataDao statisDataDao, Utils utils,CfgData cfgData) {
        this.statisDataDao = statisDataDao;
        this.utils = utils;
        this.cfgData = cfgData;
    }

    @Override
    public List<String> getUnitList() {

        List<String> unitNameList = new ArrayList<>();
        for (UnitInfo ui : cfgData.getAllUnitInfo())
            unitNameList.add(ui.getName());

        return unitNameList;
    }

    @Override
    public List<Cumulant> getDataByUnitName(String unitName) {
        int index = -1;
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

            if(-1 == (index = findAcStatisDataById(ids[i],todayStatisDatas)))
                cumulant.setToday(-1);
            else
                cumulant.setToday(todayStatisDatas[index].getAccValue().getValue());

            if(-1 == (index = findAcStatisDataById(ids[i],lastdayStatisDatas)))
                cumulant.setLastday(-1);
            else
                cumulant.setLastday(lastdayStatisDatas[index].getAccValue().getValue());

            if(-1 == (index = findAcStatisDataById(ids[i],thisMonthStatisDatas)))
                cumulant.setLastday(-1);
            else
                cumulant.setLastday(thisMonthStatisDatas[index].getAccValue().getValue());

            if(-1 == (index = findAcStatisDataById(ids[i],lastMonthStatisDatas)))
                cumulant.setLastday(-1);
            else
                cumulant.setLastday(lastMonthStatisDatas[index].getAccValue().getValue());

            list.add(cumulant);
        }

        return list;
    }

    @Override
    public List<Cumulant> getCumulantDataByUnitNameAndTime(Date stime, Date etime, String unitName){
        List<Cumulant> list = getDataByUnitName(unitName);

        AcStatisData[] queryStatisDatas = getDataByUnitNameAndTime(stime, etime, unitName);

        int index = -1;
        for (int i = 0; i < list.size(); i++)
            if(-1 == (index = findAcStatisDataById(list.get(i).getId(),queryStatisDatas)))
                list.get(i).setStatis(-1);
            else
                list.get(i).setStatis(queryStatisDatas[index].getAccValue().getValue());

        return list;
    }

    /***
     * 查询某个id在电度统计数组中的索引，如找到返回索引，否则返回-1
     * */
    private int findAcStatisDataById(int id,AcStatisData[] datas){
        if(datas == null || datas.length == 0)
            return -1;

        for(int i = 0; i < datas.length; i++)
            if(datas[i].getId() == id)
                return i;
        return -1;
    }

    private AcStatisData[] getDataByUnitNameAndTime(Date stime, Date etime, String unitName) {
        Integer[] ids = utils.getAcIdsByUnitName(unitName);
        return statisDataDao.getAcStatisData(ids, utils._DATE_FORMAT_.format(stime), utils._DATE_FORMAT_.format(etime));
    }

}
