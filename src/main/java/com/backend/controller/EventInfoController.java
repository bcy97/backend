package com.backend.controller;

import com.backend.service.EventInfoService;
import com.backend.vo.EventInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public EventInfo[] getEventByTimeAndUnitNames(@RequestBody Map<String, String> data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Gson gson = new Gson();
        List<String> unitnames = gson.fromJson(data.get("unitname"), new TypeToken<List<String>>() {
        }.getType());
        try {
            return eventInfoService.getEventByTimeAndUnitNames(sdf.parse(data.get("stime")), sdf.parse(data.get("etime")), unitnames);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
