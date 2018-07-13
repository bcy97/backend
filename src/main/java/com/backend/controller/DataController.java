package com.backend.controller;

import com.backend.service.CommService;
import com.backend.util.CfgData;
import com.backend.vo.AcValue;
import com.backend.vo.AnValue;
import com.backend.vo.StValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    CommService commService;

    @Autowired
    CfgData cfgData;

    /***
     * 获取当前遥信状态
     * */
    @RequestMapping("/getStData")
    public int getStData(String name) {
//        int id = cfgData.getStID(name);
//        Object[] datas = commService.getRealData(new int[]{id});
//        Map<String, Double> map = new HashMap<>();
//        int num = 0;
//        for (Object o : datas) {
//            if (o instanceof Integer)
//                map.put("button" + num, ((Integer) o).doubleValue());
//            else if (o instanceof AnValue)
//                map.put("button" + num, (double) ((AnValue) o).getValue());
//            else if (o instanceof StValue)
//                map.put("button" + num, (double) ((StValue) o).getValue());
//            else if (o instanceof AcValue)
//                map.put("button" + num, (double) ((AcValue) o).gethValue());
//            num++;
//        }
        int state = (int) (Math.random() * 2);
        return state;
    }

    /**
     * 获取当前遥测状态
     *
     * @param name
     * @return
     */
    @RequestMapping("/getAnData")
    public Map<String, Double> getAnData(String name) {
        int id = cfgData.getAnID(name);
        Object[] datas = commService.getRealData(new int[]{id});
        Map<String, Double> map = new HashMap<String, Double>();
        int num = 0;
        for (Object o : datas) {
            if (o instanceof Integer)
                map.put("button" + num, ((Integer) o).doubleValue());
            else if (o instanceof AnValue)
                map.put("button" + num, (double) ((AnValue) o).getValue());
            else if (o instanceof StValue)
                map.put("button" + num, (double) ((StValue) o).getValue());
            else if (o instanceof AcValue)
                map.put("button" + num, (double) ((AcValue) o).gethValue());
            num++;
        }
        return map;
    }

    /**
     * 获取当前电度状态
     *
     * @param name
     * @return
     */
    @RequestMapping("/getAcData")
    public Map<String, Double> getAcData(String name) {
        int id = cfgData.getAcID(name);
        Object[] datas = commService.getRealData(new int[]{id});
        Map<String, Double> map = new HashMap<String, Double>();
        int num = 0;
        for (Object o : datas) {
            if (o instanceof Integer)
                map.put("button" + num, ((Integer) o).doubleValue());
            else if (o instanceof AnValue)
                map.put("button" + num, (double) ((AnValue) o).getValue());
            else if (o instanceof StValue)
                map.put("button" + num, (double) ((StValue) o).getValue());
            else if (o instanceof AcValue)
                map.put("button" + num, (double) ((AcValue) o).gethValue());
            num++;
        }
        return map;
    }

    @RequestMapping("/getAnDataByPic")
    public Map<String, Double> getDataByPicture(String name) {

        int[] anId = Arrays.stream(cfgData.getAnIDByPic(name))
                .mapToInt(Integer::valueOf).toArray();
        Object[] anData = commService.getRealData(anId);
        Map<String, Double> map = new HashMap<String, Double>();
        int num = 0;
        for (Object o : anData) {
            if (o instanceof Integer)
                map.put("button" + num, ((Integer) o).doubleValue());
            else if (o instanceof AnValue)
                map.put("button" + num, (double) ((AnValue) o).getValue());
            else if (o instanceof StValue)
                map.put("button" + num, (double) ((StValue) o).getValue());
            else if (o instanceof AcValue)
                map.put("button" + num, (double) ((AcValue) o).gethValue());
            num++;
        }
        return map;
    }
}
//        int[] acId = Arrays.stream(cfgData.getAcIDByPic(name))
//                .mapToInt(Integer::valueOf).toArray();
//        int[] stId = Arrays.stream(cfgData.getStIDByPic(name))
//                .mapToInt(Integer::valueOf).toArray();
//        int id = cfgData.getAnID(点名);// 通过遥测点名获取遥测的id
//        int id = cfgData.getStID(点名);// 通过遥信点名获取遥信的id

////		 通过id数组进行拿数据
//        Object[] datas = commService.getRealData(new int[]{id});
////		 数据的顺序为请求id的顺序，是相对应的
//        if (null == datas)
//            return;
//        int num = 1;
//        Map<String, Double> map = new HashMap<String, Double>();
//        for (Object o : datas) {
//            if (o instanceof Integer)
//                map.put("button" + num, ((Integer) o).doubleValue());
//            if (o instanceof AnValue)
//                map.put("button" + num, (double) ((AnValue) o).getValue());
//            if (o instanceof StValue)
//                map.put("button" + num, (double) ((StValue) o).getValue());
//            if (o instanceof AcValue)
//                map.put("button" + num, (double) ((AcValue) o).gethValue());
//            num++;
//        }
//    }
