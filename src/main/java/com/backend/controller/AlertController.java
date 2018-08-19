package com.backend.controller;

import com.backend.service.AlertService;
import com.backend.vo.EventInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alert")
public class AlertController {

    private AlertService alertService;

    @Autowired
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @RequestMapping("/getAlert")
    public EventInfo[] getImportantAlert() {
        return alertService.getImportantAlert();
    }
}
