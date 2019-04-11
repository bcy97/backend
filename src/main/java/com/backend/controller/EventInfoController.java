package com.backend.controller;

import com.backend.service.EventInfoService;
import com.backend.vo.EventInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eventInfo")
public class EventInfoController {
    private EventInfoService eventInfoService;

    @Autowired
    public EventInfoController(EventInfoService eventInfoService) {
        this.eventInfoService = eventInfoService;
    }

    @RequestMapping(value = "/getInfo", consumes = "application/json")
    public EventInfo[] getEventByTimeAndUnitNames(@RequestBody Map<String, String> data,@RequestBody String companyId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Gson gson = new Gson();
        List<String> unitnames = gson.fromJson(data.get("unitname"), new TypeToken<List<String>>() {
        }.getType());


        try {
            // type 0:遥信    1:遥测
            return eventInfoService.getEventByTimeAndUnitNames(sdf.parse(data.get("stime")), sdf.parse(data.get("etime")), unitnames, new Integer(data.get("type")), companyId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value="/getStEventInfo",consumes="application/json")
    public EventInfo[] getStEventInfoByEname(@RequestBody Map<String, Object> map,@RequestBody String companyId){
    	Calendar calNow = Calendar.getInstance();
    	Calendar calBef = Calendar.getInstance();
    	calBef.add(Calendar.MONTH, -3);
    	Date begTime = calBef.getTime();
    	Date endTime = calNow.getTime();
    	
    	String ename = map.get("ename").toString();
    	if(map.containsKey("begTime")) {
    		begTime = (Date)map.get("begTime");
    	}
    	if(map.containsKey("endTime")) {
    		endTime = (Date)map.get("endTime");
    	}
    	
    	System.out.println("ename:" + ename);
    	return eventInfoService.getStEventByEname(begTime, endTime, ename, companyId);
    }
}
