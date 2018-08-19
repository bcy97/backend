package com.backend.controller;

import com.backend.service.EventInfoService;
import com.backend.vo.EventInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/eventInfo")
public class EventInfoController {
    private EventInfoService eventInfoService;

    @Autowired
    public EventInfoController(EventInfoService eventInfoService) {
        this.eventInfoService = eventInfoService;
    }

    @RequestMapping("/getInfo")
    public EventInfo[] getEventByTimeAndPointName(@RequestBody Date stime, @RequestBody Date etime, @RequestBody String pointName) {
        return eventInfoService.getEventByTimeAndPointName(stime, etime, pointName);
    }
}
