package com.backend.controller;

import com.backend.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private MD5 mdUtil;

    public File[] download(ArrayList<String> filename) {
        return null;
    }

    @RequestMapping("/compare")
    public boolean compare(@RequestBody String md5) {

        try {
            // 取得根目录路径
            String rootPath = System.getProperty("user.dir");
    //        File configList = ResourceUtils.getFile("classpath:static/configList.xml");
            File configList = ResourceUtils.getFile(rootPath + "//static//configList.xml");
            if (md5 != null && md5.equals(mdUtil.getMD5(configList))) {
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

}
