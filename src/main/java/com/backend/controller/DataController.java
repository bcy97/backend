package com.backend.controller;

import com.backend.service.CommService;
import com.backend.util.CfgData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    CommService commService;

    @Autowired
    CfgData cfgData;

//    @RequestMapping("/getData")
//    public Object[] getData() {
//        int[] ids = new int[10];
//        for (short i = 0; i < 10; i++)
//            ids[i] = Utils.getId((byte) 1, (short) 1, i);
//        return cs.getRealData(ids);
//    }

    /***
     * 获取当前状态
     * */
    @RequestMapping("/getNowState")
    public int getNowState( String name) {
        // 需把点名转为id 可通过
        int id = cfgData.getAcID(name);// 通过电度点名获取电度的id
        Object[] datas = commService.getRealData(new int[]{id});
        return (int) datas[0];
    }
}
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
