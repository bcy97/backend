package com.backend.util;

import com.backend.vo.AnO;
import com.backend.vo.StO;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class Evfault {
    private static Map<String, List<AnO>> anTempMap = new HashMap<>();
    private static Map<String, List<StO>> stTempMap = new HashMap<>();

    private Evfault() {
        initEvfault();
    }

    private void initEvfault() {
        File file = null;
        String rootPath = System.getProperty("user.dir");
        try {
            file = ResourceUtils.getFile(rootPath + "//evfault.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(file == null || !file.exists())
            return;

        Pattern intPattern = Pattern.compile("^[0-9]+$");
        Pattern doublePattern = Pattern.compile("^[0-9]+([.][0-9]+){0,1}$");

        ReadINIFile rif = null;
        try {
            rif = new ReadINIFile(file.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> list = rif.getAllSections();
        for (String item : list) {
            if (rif.sectionContainsKey(item, "mode")) {
                float iFi = 1;
                float inFi = 1;
                float uFi = 1;
                float unFi = 1;
                // mode=mode1,I:8,In:2

                String[] arr = rif.getValue(item, "mode").trim().split(",");
                if (arr == null || arr.length <= 0)
                    continue;
                String tempName = arr[0];
                List<String> keys = rif.sectionGetKeys(tempName);
                if (keys == null)
                    continue;

                for (int i = 1; i < arr.length; i++) {
                    String[] arrTemp = arr[i].split(":");
                    if (arrTemp != null && arrTemp.length >= 2) {
                        if (arrTemp[0].toLowerCase().trim().equals("i")) {
                            if (intPattern.matcher(arrTemp[1]).matches())
                                iFi = new Float(arrTemp[1]);
                        } else if (arrTemp[0].toLowerCase().trim().equals("in")) {
                            if (doublePattern.matcher(arrTemp[1]).matches())
                                inFi = new Float(arrTemp[1]);
                        } else if (arrTemp[0].toLowerCase().trim().equals("u")) {
                            if (doublePattern.matcher(arrTemp[1]).matches())
                                uFi = new Float(arrTemp[1]);
                        } else if (arrTemp[0].toLowerCase().trim().equals("un")) {
                            if (doublePattern.matcher(arrTemp[1]).matches())
                                unFi = new Float(arrTemp[1]);
                        }
                    }
                }
                for (String key : keys) {
                    String value = rif.getValue(tempName, key);
                    if (null == value || value.isEmpty())
                        continue;
                    arr = value.split(",");
                    if (key.contains("an")) {
                        AnO ano = new AnO();
                        ano.setCname(arr[0]);
                        ano.setPoinum((byte) 0);
                        if (arr.length > 1 && intPattern.matcher(arr[1]).matches())
                            ano.setPoinum(new Byte(arr[1]));
                        if (arr[0].contains("零序电流"))
                            ano.setFi(inFi);
                        else if (arr[0].contains("零序电压"))
                            ano.setFi(unFi);
                        else if (arr[0].contains("电流"))
                            ano.setFi(iFi);
                        else if (arr[0].contains("电压"))
                            ano.setFi(uFi);
                        else
                            ano.setFi(1);
                        /// 如果遥测模板中没有包含该模板名，就添加
                        if (!anTempMap.containsKey(item))
                            anTempMap.put(item, new ArrayList<AnO>());
                        anTempMap.get(item).add(ano);
                    } else if (key.contains("st"))// 该键包含st
                    {
                        StO sto = new StO();
                        sto.setCname(arr[0]);
                        sto.setType((byte) 0);
                        if (arr.length > 1 && intPattern.matcher(arr[1]).matches())
                            sto.setType(new Byte(arr[1]));
                        if (!stTempMap.containsKey(item))
                            stTempMap.put(item, new ArrayList<StO>());
                        stTempMap.get(item).add(sto);
                    }
                }

            }
        }

    }

    public List<AnO> getAnTemp(String key) {
        if (anTempMap.containsKey(key))
            return anTempMap.get(key);
        return null;
    }

    public List<StO> getStTemp(String key) {
        if (stTempMap.containsKey(key))
            return stTempMap.get(key);
        return null;
    }
}
