package com.backend.service.impl;

import com.backend.dao.RealDataDao;
import com.backend.service.RealDataService;
import com.backend.util.CfgData;
import com.backend.vo.AcO;
import com.backend.vo.AnO;
import com.backend.vo.AnValue;
import com.backend.vo.StO;
import com.backend.vo.UnitInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
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
    public Map<String, AnValue> getRealData(String unitName, String companyId) {

        List<UnitInfo> list = cfgData.getAllUnitInfo(companyId);
        Integer[] ids = new Integer[0];
        for (UnitInfo ui : list) {
            if (unitName.equals(ui.getName())) {
                List<AnO> anoList = cfgData.getAnOByUnitNo(ui.getUnitNo(), companyId);
                ids = new Integer[anoList.size()];
                for (int i = 0; i < ids.length; i++)
                    ids[i] = anoList.get(i).getId();
                break;
            }
        }
        System.out.println("查询中...");
        AnValue[] anValues = realDataDao.getAnRealData(ids, companyId);
        System.out.println("获得数据");
        Map<String, AnValue> map = new LinkedHashMap<>();

        for (int i = 0; i < ids.length; i++) {
            //valid 不为0，要对该值进行上下限判断,为越上限设valid为2,越下限设valid为3
            if(0 != anValues[i].getValid()){
                if(anValues[i].getValue() > cfgData.getAnO(ids[i], companyId).getUpV())
                    anValues[i].setValid((byte)2);
                else if(anValues[i].getValue() < cfgData.getAnO(ids[i], companyId).getDwV())
                    anValues[i].setValid((byte)3);
            }
            map.put(cfgData.getAnO(ids[i], companyId).getSname(), anValues[i]);
        }
        System.out.println("返回数据");
        return map;
    }

    @Override
	public Object[] getRealDataByEnames(String[] enames, String companyId) {
		// TODO Auto-generated method stub
		AnO ano = null;
		AcO aco = null;
		StO sto = null;
		String ename = null;
		Integer[] ids = new Integer[enames.length];
		for (int i = 0; i < enames.length; i++) {
			ename = enames[i];
			if("".equals(ename)) {
				ids[i] = 0xFFFFFF;
				continue;
			}
			
			if(null == (ano = cfgData.getAnO(ename, companyId))) {
				if(null == (aco = cfgData.getAcO(ename, companyId))) {
					if(null == (sto = cfgData.getStO(ename, companyId))) {
						ids[i] = 0xFFFFFF;
						continue;
					}
					ids[i] = sto.getId();
					continue;
				}
				ids[i] = aco.getId();
				continue;
			}
			ids[i] = ano.getId();
			
		}
		
		return realDataDao.getRealData(ids, companyId);
	}
}
