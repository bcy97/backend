package com.backend.controller;

import com.backend.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public Map<String, Float> getLineData(@RequestBody String[] pointName) {
        return lineService.getRealLineData(pointName);
    }


    @RequestMapping(value = "/getHistoryLineData", consumes = "application/json")
    public Map<String, Float[]> getHistoryData(@RequestBody String time, @RequestBody String[] pointName) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            Date stime = sdf.parse(time + " 00:00");
            Date etime = sdf.parse(time + " 23:59");
            return lineService.getHistoryLineData(stime, etime, pointName);
        } catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
