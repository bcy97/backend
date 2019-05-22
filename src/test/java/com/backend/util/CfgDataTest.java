package com.backend.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CfgDataTest {

    @Autowired
    CfgData cfgData;

    @Test
    public void testGetId() {
    //    int id = cfgData.getAnID("NPTUa1");
     //   System.out.println(id);
    }

    @Test
    public void testGetAnId() {
//        Integer[] ids = cfgData.getAnIDByPic("1#开闭所201柜1#进线");
//        for (int i = 0; i < ids.length; i++) {
//            System.out.print(ids[i] + " ");
//        }
    }
}
