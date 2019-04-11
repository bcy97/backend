package com.backend.controller;

import com.backend.service.RealDataService;
import com.backend.util.Utils;
import com.backend.vo.AnValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/realdata") //realdata ：获取实时数据
public class RealDataController {

    private RealDataService realDataService;

    @Autowired
    public RealDataController(RealDataService realDataService) {
        this.realDataService = realDataService;
    }

    @RequestMapping(value = "/getRealData", consumes = "application/json")
    public Map<String, AnValue> getRealData(@RequestBody String unitName,@RequestBody String companyId) {

        System.out.println(unitName);

        return realDataService.getRealData(unitName, companyId);

    }
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss sss");
    @RequestMapping(value="/getRealDataByEnameList",method=RequestMethod.POST,consumes = "application/json")
    public Map<String,Object[]> getRealDataByEnameList(@RequestBody Map<String, String[]> map,@RequestBody String companyId){
    	String[] enames = map.get("ename[]");
    	if(enames == null) {
    		System.out.println("enames is null");
    	}else {
    		StringBuilder stringBuilder = new StringBuilder("时间:" + System.currentTimeMillis() + " " + "ename[]:");
    		for (String ename : enames) {
    			if(Utils.isNull(ename)) {
    				stringBuilder.append("null ");
    			}else {
    				stringBuilder.append(ename + " ");
    			}
			}
    		System.out.println(stringBuilder.toString());
    	}
    	
    	if(enames == null || enames.length == 0) return null;
    	
    	Object[] objArr = realDataService.getRealDataByEnames(enames, companyId);
    	Map<String, Object[]> responseMap = new HashMap<>();
    	responseMap.put("message", objArr);
    	return responseMap;
    }
}
