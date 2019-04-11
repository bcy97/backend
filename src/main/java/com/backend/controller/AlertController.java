package com.backend.controller;

import com.backend.service.AlertService;
import com.backend.vo.EventInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/alert")
public class AlertController {

    private AlertService alertService;

    @Autowired
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @RequestMapping("/getAlert")
    public EventInfo[] getImportantAlert(@RequestBody Map<String, String> map) {
        return alertService.getImportantAlert(map.get("companyId"));
    }
}
