package com.backend.controller;

import com.backend.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private MD5 mdUtil;


    String rootPath = System.getProperty("user.dir");

    @RequestMapping("/compare")
    public boolean compare(@RequestBody Map<String,String> data) {

        try {
            String md5 = data.get("md5");
            // 取得根目录路径
            File configList = ResourceUtils.getFile(rootPath + data.get("companyId") + "/baseConfigs/configList.xml");
            if (md5 != null && md5.equals(mdUtil.getMD5(configList))) {
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    @RequestMapping("/getIconLibrary")
    public Map<String, List<String>> getIconLibrary(@RequestBody Map<String,String> data) {
        File publicLibrary = new File(rootPath + "/iconlibrary/public");
        File userLibrary = new File(rootPath + "/iconlibrary/" + data.get("companyId"));

        Map<String, List<String>> picList = new HashMap<>();
        picList.put("public", Arrays.asList(publicLibrary.list()));
        picList.put(data.get("companyId"), Arrays.asList(userLibrary.list()));

        return picList;

    }

}
