package com.backend.service.impl;

import com.backend.dao.RealDataDao;
import com.backend.service.RealDataService;
import com.backend.util.CfgData;
import com.backend.vo.AnO;
import com.backend.vo.AnValue;
import com.backend.vo.UnitInfo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RealDataServiceImpl implements RealDataService {

    static Logger logger = Logger.getLogger("RealDataServiceImpl");

    private RealDataDao realDataDao;

    public RealDataServiceImpl(){}

    public RealDataServiceImpl(RealDataDao realDataDao){
        this.realDataDao = realDataDao;
    }

    @Override
    public Map<String, AnValue> getRealData(String unitName) {
        CfgData cfgData = new CfgData();

        List<UnitInfo> list = cfgData.getAllUnitInfo();
        Integer[] ids = new Integer[0];
        for(UnitInfo ui : list){
            if(unitName.equals(ui.getName())){
                List<AnO> anoList = cfgData.getAnOByUnitNo(ui.getUnitNo());
                ids = new Integer[anoList.size()];
                for(int i = 0; i < ids.length; i++)
                    ids[i] = anoList.get(i).getId();
                break;
            }
        }
        AnValue[] anValues = realDataDao.getAnRealData(ids);

        Map<String,AnValue> map = new HashMap<>();

        for(int i = 0; i < ids.length; i ++)
            map.put(cfgData.getAnO(ids[i]).getSname(),anValues[i]);

        return map;
    }

}
