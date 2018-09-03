package com.backend.controller;

import com.backend.service.RealDataService;
import com.backend.vo.AnValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/realdata")
public class RealDataController {

    private RealDataService realDataService;

    @Autowired
    public RealDataController(RealDataService realDataService) {
        this.realDataService = realDataService;
    }

    @RequestMapping(value = "/getRealData", consumes = "application/json")
    public Map<String, AnValue> getRealData(@RequestBody String unitName) {

        System.out.println(unitName);

        return realDataService.getRealData(unitName);

    }
}
