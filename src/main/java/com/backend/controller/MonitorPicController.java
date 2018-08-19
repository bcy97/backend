package com.backend.controller;

import com.backend.service.MonitorPicService;
import com.backend.util.CfgData;
import com.backend.vo.AcValue;
import com.backend.vo.AnValue;
import com.backend.vo.StValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/monitor")
public class MonitorPicController {

    private MonitorPicService monitorPicService;
    private CfgData cfgData;

    @Autowired
    public MonitorPicController(MonitorPicService monitorPicService, CfgData cfgData) {
        this.monitorPicService = monitorPicService;
        this.cfgData = cfgData;
    }

    @RequestMapping("/getPicList")
    public List<String> getPicList() {
        return monitorPicService.getPicList();
    }

    @RequestMapping(value = "/getAnData", consumes = "application/json")
    public Map<String, AnValue> getAnData(@RequestBody String[] ids) {

        Map<String, AnValue> result = new HashMap<>();

        Integer[] id = new Integer[ids.length];
        for (int i = 0; i < ids.length; i++) {
            id[i] = cfgData.getAnID(ids[i]);
        }

        Object[] data = monitorPicService.getRealData(id);
        for (int i = 0; i < data.length; i++) {
            try {

                result.put(ids[i], (AnValue) data[i]);
            } catch (Exception e) {
                result.put(ids[i], new AnValue((byte) 0, 0));
            }
        }

        return result;
    }

    /**
     * 获取遥信量
     *
     * @param ids 遥信量Id
     * @return Map<String, StValue>为遥信量id，value为遥信值VO
     */
    @RequestMapping(value = "/getStData", consumes = "application/json")
    public Map<String, StValue> getStData(@RequestBody String[] ids) {

        Map<String, StValue> result = new HashMap<>();

        Integer[] id = new Integer[ids.length];
        for (int i = 0; i < ids.length; i++) {
            id[i] = cfgData.getStID(ids[i]);
        }

        Object[] data = monitorPicService.getRealData(id);
        for (int i = 0; i < data.length; i++) {
            try {
                result.put(ids[i], (StValue) data[i]);
            } catch (Exception e) {
                result.put(ids[i], new StValue((byte) 1, (byte) 0));
                System.out.println(ids[i] + " " + id[i]);
            }
        }

        System.out.println("--------------------------------------");

        return result;
    }

    /**
     * 获取电度量
     *
     * @param ids 遥信量Id
     * @return Map<String, StValue>为遥信量id，value为遥信值VO
     */
    @RequestMapping(value = "/getAcData", consumes = "application/json")
    public Map<String, AcValue> getAcData(@RequestBody String[] ids) {

        Map<String, AcValue> result = new HashMap<>();

        Integer[] id = new Integer[ids.length];
        for (int i = 0; i < ids.length; i++) {
            id[i] = cfgData.getAnID(ids[i]);
        }

        Object[] data = monitorPicService.getRealData(id);
        for (int i = 0; i < data.length; i++) {
            result.put(ids[i], (AcValue) data[i]);
        }

        return result;
    }
}
