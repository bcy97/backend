package com.backend.controller;

import com.backend.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Map<Date, Float> getLineData(@RequestBody String picName) {
        return lineService.getRealLineData(picName);
    }


    @RequestMapping(value = "/getHistoryLineData", consumes = "application/json")
    public Map<Date, Float> getHistoryData(@RequestBody Date stime, @RequestBody Date etime, @RequestBody String picName) {
        return lineService.getHistoryLineData(stime, etime, picName);
    }
}
