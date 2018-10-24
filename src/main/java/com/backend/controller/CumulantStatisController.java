package com.backend.controller;

import com.backend.service.CumulantStatisService;
import com.backend.vo.Cumulant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cumulant")
public class CumulantStatisController {

    private CumulantStatisService cumulantStatisService;

    @Autowired
    public CumulantStatisController(CumulantStatisService cumulantStatisService) {
        this.cumulantStatisService = cumulantStatisService;
    }

    @RequestMapping(value = "/unitlist", consumes = "application/json")
    public List<String> getUnitList() {
        return cumulantStatisService.getUnitList();
    }

    @RequestMapping(value = "/getDataByUnitName", consumes = "application/json")
    public List<Cumulant> getDataByUnitName(@RequestBody String unitName) {
        System.out.println(unitName);
        return cumulantStatisService.getDataByUnitName(unitName);
    }

    @RequestMapping(value = "/getDataByUnitNameAndTime", consumes = "application/json")
    public List<Cumulant> getDataByUnitNameAndTime(@RequestBody Map<String, String> data) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            return cumulantStatisService.getCumulantDataByUnitNameAndTime(sdf.parse(data.get("stime")), sdf.parse(data.get("etime")), data.get("unitname"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
