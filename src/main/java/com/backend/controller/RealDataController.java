package com.backend.controller;

import com.backend.service.RealDataService;
import com.backend.util.CfgData;
import com.backend.vo.AnValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/realData")
public class RealDataController {

    @Autowired
    RealDataService realDataService;
    @Autowired
    CfgData cfgData;

    @RequestMapping(value = "/getAnData", consumes = "application/json")
    public Map<String, AnValue> getAnData(@RequestParam(value = "ids", required = false) String[] ids) {

        Map<String, AnValue> result = new HashMap<>();

        int[] id = new int[ids.length];
//        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i < ids.length; i++) {
            id[i] = cfgData.getAnID(ids[i]);
//            map.put(ids[i], id[i]);
        }

        Object[] data = realDataService.getRealData(id);
        for (int i = 0; i < data.length; i++) {
            result.put(ids[i], (AnValue) data[i]);
        }

        return result;
    }
}
