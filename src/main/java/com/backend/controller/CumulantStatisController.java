package com.backend.controller;

import com.backend.service.CumulantStatisService;
import com.backend.vo.Cumulant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/cumulant")
public class CumulantStatisController {

    private CumulantStatisService cumulantStatisService;

    @Autowired
    public CumulantStatisController(CumulantStatisService cumulantStatisService) {
        this.cumulantStatisService = cumulantStatisService;
    }

    @RequestMapping("/unitlist")
    public List<String> getUnitList() {
        return cumulantStatisService.getUnitList();
    }

    @RequestMapping(value = "/unitName", consumes = "application/json")
    public List<Cumulant> getDataByUnitName(@RequestBody String unitName) {
        return cumulantStatisService.getDataByUnitName(unitName);
    }

    public List<Cumulant> getDataByUnitNameAndTime(@RequestBody Date stime, @RequestBody Date etime, @RequestBody String unitName) {
        return cumulantStatisService.getDataByUnitNameAndTime(stime, etime, unitName);
    }
}
