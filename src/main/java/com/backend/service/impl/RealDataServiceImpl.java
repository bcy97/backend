package com.backend.service.impl;

import com.backend.dao.RealDataDao;
import com.backend.service.RealDataService;
import com.backend.util.CfgData;
import com.backend.vo.AnO;
import com.backend.vo.AnValue;
import com.backend.vo.UnitInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RealDataServiceImpl implements RealDataService {

    static Logger logger = Logger.getLogger("RealDataServiceImpl");

    private RealDataDao realDataDao;
    private CfgData cfgData;

    @Autowired
    public RealDataServiceImpl(RealDataDao realDataDao, CfgData cfgData) {
        this.realDataDao = realDataDao;
        this.cfgData = cfgData;
    }

    @Override
    public Map<String, AnValue> getRealData(String unitName) {

        List<UnitInfo> list = cfgData.getAllUnitInfo();
        Integer[] ids = new Integer[0];
        for (UnitInfo ui : list) {
            if (unitName.equals(ui.getName())) {
                List<AnO> anoList = cfgData.getAnOByUnitNo(ui.getUnitNo());
                ids = new Integer[anoList.size()];
                for (int i = 0; i < ids.length; i++)
                    ids[i] = anoList.get(i).getId();
                break;
            }
        }
        AnValue[] anValues = realDataDao.getAnRealData(ids);

        Map<String, AnValue> map = new HashMap<>();

        for (int i = 0; i < ids.length; i++) {
            //valid 不为0，要对该值进行上下限判断,为越上限设valid为2,越下限设valid为3
            if(0 != anValues[i].getValid()){
                if(anValues[i].getValue() > cfgData.getAnO(ids[i]).getUpV())
                    anValues[i].setValid((byte)2);
                else if(anValues[i].getValue() < cfgData.getAnO(ids[i]).getDwV())
                    anValues[i].setValid((byte)3);
            }
            map.put(cfgData.getAnO(ids[i]).getSname(), anValues[i]);
        }

        return map;
    }

}
