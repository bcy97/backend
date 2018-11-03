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

    public File[] download(ArrayList<String> filename) {
        return null;
    }

    String rootPath = System.getProperty("user.dir");

    @RequestMapping("/compare")
    public boolean compare(@RequestBody String md5) {

        try {
            // 取得根目录路径
            File configList = ResourceUtils.getFile(rootPath + "/baseConfigs/configList.xml");
            if (md5 != null && md5.equals(mdUtil.getMD5(configList))) {
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    @RequestMapping("/getIconLibrary")
    public Map<String, List<String>> getIconLibrary() {
        File publicLibrary = new File(rootPath + "/iconlibrary/public");
        File userLibrary = new File(rootPath + "/iconlibrary/user");

        Map<String, List<String>> picList = new HashMap<>();
        picList.put("public", Arrays.asList(publicLibrary.list()));
        picList.put("user", Arrays.asList(userLibrary.list()));

        return picList;

    }

}
