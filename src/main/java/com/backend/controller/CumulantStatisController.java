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
    public List<String> getUnitList(@RequestBody Map<String, String> map) {
        return cumulantStatisService.getUnitList(map.get("companyId"));
    }

    @RequestMapping(value = "/getDataByUnitName", consumes = "application/json")
    public List<Cumulant> getDataByUnitName(@RequestBody Map<String,String> map) {

        /**
         * 请求参数类型
         * unitName : 中心电站
         * companyId : demo
         * */

        System.out.println(map.get("unitName"));
        return cumulantStatisService.getDataByUnitName(map.get("unitName"), map.get("companyId"));
    }

    @RequestMapping(value = "/getDataByUnitNameAndTime", consumes = "application/json")
    public List<Cumulant> getDataByUnitNameAndTime(@RequestBody Map<String, String> data) {

        /**
         * 请求参数类型
         * companyId : demo
         * unitName : 中心电站
         * sTime : 2017-7-2 00:01
         * eTime : 2017-7-2 23:59
         * */

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            return cumulantStatisService.getCumulantDataByUnitNameAndTime(sdf.parse(data.get("sTime")), sdf.parse(data.get("eTime")), data.get("unitName"),data.get("companyId"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
