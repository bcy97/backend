package com.backend.controller;

import com.backend.service.LineService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/line")
public class LineController {

    private LineService lineService;

    @Autowired
    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @RequestMapping(value = "/getRealLineData", consumes = "application/json")
    public Map<String, Float> getLineData(@RequestBody Map<String,String> data) {   //原先结构为Map<String, Object> data，现在改为Map<String, String>

        Gson gson = new Gson();
//        String[] pointName = (String[]) data.get("pointName");
        String[] pointName = gson.fromJson(data.get("pointName"), new TypeToken<String[]>() {}.getType());
        String companyId = data.get("companyId");
        System.out.println(pointName);
        return lineService.getRealLineData(pointName, companyId);
    }


    @RequestMapping(value = "/getHistoryLineData", consumes = "application/json")
    public Map<String, Float[]> getHistoryData(@RequestBody Map<String, String> data) {

        Gson gson = new Gson();
        String[] pointName = gson.fromJson(data.get("pointName"), new TypeToken<String[]>() {
        }.getType());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            Date stime = sdf.parse(data.get("time") + " 00:00");       //时间格式为: 2019-6-12 00:00
            Date etime = sdf.parse(data.get("time") + " 23:59");
//           Date time = sdf.parse(data.get("time") + " 00:00");
            return lineService.getHistoryLineData(stime, etime, pointName, data.get("companyId"));
        } catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
