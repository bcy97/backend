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
        int id = cfgData.getAcID("N05_AC");
        System.out.println(id);
    }
}
