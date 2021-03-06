package com.backend.controller;

import com.backend.service.MonitorPicService;
import com.backend.util.CfgData;
import com.backend.vo.AcValue;
import com.backend.vo.AnValue;
import com.backend.vo.StValue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
    private Gson gson;

    @Autowired
    public MonitorPicController(MonitorPicService monitorPicService, CfgData cfgData) {
        this.monitorPicService = monitorPicService;
        this.cfgData = cfgData;
        this.gson = new Gson();
    }

    @RequestMapping("/getPicList")
    public List<String> getPicList(@RequestBody String companyId) {
        return monitorPicService.getPicList(companyId);
    }

    @RequestMapping(value = "/getAnData", consumes = "application/json")
    public Map<String, AnValue> getAnData(@RequestBody Map<String, String> map) {

        System.out.println(map.get("ids"));
        String[] ids = gson.fromJson(map.get("ids"), new TypeToken<String[]>() {
        }.getType());
        String companyId = map.get("companyId");

        System.out.println("An--------------------------------------");
        Map<String, AnValue> result = new HashMap<>();

        Integer[] id = new Integer[ids.length];
        for (int i = 0; i < ids.length; i++) {
            id[i] = cfgData.getAnID(ids[i], companyId);
            if(-1 == id[i])
                id[i] = cfgData.getAcID(ids[i],companyId);
        }

        Object[] data = monitorPicService.getRealData(id, companyId);
        for (int i = 0; i < data.length; i++) {
            if(data[i] instanceof AnValue) {
                result.put(ids[i], (AnValue) data[i]);
            } else if(data[i] instanceof  AcValue){
                float anV = (float)((AcValue)data[i]).getValue();
                byte valid = ((AcValue)data[i]).getValid();
                result.put(ids[i],new AnValue(valid,anV));
            }else{
                result.put(ids[i], new AnValue((byte) 0, 0));
            }

//            try {
//
//                result.put(ids[i], (AnValue) data[i]);
//            } catch (Exception e) {
//                result.put(ids[i], new AnValue((byte) 0, 0));
//            }
        }

        return result;
    }

    /**
     * 获取遥信量
     *
     * @param map 公司名：companyId，遥信id：ids
     * @return Map为遥信量id，value为遥信值
     */
    @RequestMapping(value = "/getStData", consumes = "application/json")
    public Map<String, StValue> getStData(@RequestBody Map<String, String> map) {

//        String[] ids = (String[]) map.get("ids");
        String[] ids = gson.fromJson(map.get("ids"), new TypeToken<String[]>() {
        }.getType());
        String companyId = map.get("companyId");

        System.out.println("St--------------------------------------");
        Map<String, StValue> result = new HashMap<>();

        Integer[] id = new Integer[ids.length];
        for (int i = 0; i < ids.length; i++) {
            id[i] = cfgData.getStID(ids[i], companyId);
        }

        Object[] data = monitorPicService.getRealData(id, companyId);
        for (int i = 0; i < data.length; i++) {
            try {
                result.put(ids[i], (StValue) data[i]);
                System.out.println(ids[i] + " " + ((StValue) data[i]).getValue());
            } catch (Exception e) {
                result.put(ids[i], new StValue((byte) 1, (byte) 0));
                System.out.println(ids[i] + " " + id[i]);
            }
        }


        return result;
    }

    /**
     * 获取电度量
     *
     * @return Map<String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               StValue>为电度量id，value为电度值
     */
    @RequestMapping(value = "/getAcData", consumes = "application/json")
    public Map<String, AcValue> getAcData(@RequestBody Map<String, String> map) {

//        String[] ids = (String[]) map.get("ids");
        String[] ids = gson.fromJson(map.get("ids"), new TypeToken<String[]>() {}.getType());
        String companyId = map.get("companyId");

        System.out.println("Ac--------------------------------------");
        Map<String, AcValue> result = new HashMap<>();

        Integer[] id = new Integer[ids.length];
        for (int i = 0; i < ids.length; i++) {
            id[i] = cfgData.getAcID(ids[i], companyId);
        }

        Object[] data = monitorPicService.getRealData(id, companyId);
        for (int i = 0; i < data.length; i++) {
            result.put(ids[i], (AcValue) data[i]);
        }

        return result;
    }
}
