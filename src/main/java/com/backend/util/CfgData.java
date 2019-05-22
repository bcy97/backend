package com.backend.util;

import com.backend.vo.AcO;
import com.backend.vo.AnO;
import com.backend.vo.StO;
import com.backend.vo.UnitInfo;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Component
public class CfgData implements ApplicationRunner {

    private Logger logger = Logger.getLogger("CfgData");

    @Autowired
    private Utils utils;

    private Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");

    private Map<String, Map<String, AnO>> anMap = null;
    private Map<String, Map<String, StO>> stMap = null;
    private Map<String, Map<String, AcO>> acMap = null;
    private Map<String, Map<Integer, String>> anIdNameMap = null;
    private Map<String, Map<Integer, String>> acIdNameMap = null;
    private Map<String, Map<Integer, String>> stIdNameMap = null;
    private Map<String, List<UnitInfo>> unitList = null;
    private Map<String, Map<Short, List<AnO>>> unitAnMap = null;
    private Map<String, Map<Short, List<StO>>> unitStMap = null;
    private Map<String, Map<Short, List<AcO>>> unitAcMap = null;



//    private Map<String, AnO> anMap = null;
//    private Map<String, StO> stMap = null;
//    private Map<String, AcO> acMap = null;
//    private Map<Integer, String> anIdNameMap = null;
//    private Map<Integer, String> acIdNameMap = null;
//    private Map<Integer, String> stIdNameMap = null;
//    private List<UnitInfo> unitList = null;
//    private Map<Short, List<AnO>> unitAnMap = null;
//    private Map<Short, List<StO>> unitStMap = null;
//    private Map<Short, List<AcO>> unitAcMap = null;

//    private Map<String, String> mapper = new ConcurrentHashMap<>();
    private Map<String, Map<String, String>> mapper = new ConcurrentHashMap<>();
    
    public String getRealName(String name, String companyId) {
        if(Utils.isNull(name))
            return name;
        if(Utils.isNull(companyId))
            return name;
        if(!mapper.get(companyId).containsKey(name))
            return name;
        return mapper.get(companyId).get(name);
//    	if(!Utils.isNull(name) && !Utils.isNull(companyId)
//                && mapper.get(companyId).containsKey(name))  //!mapper.containsKey(name)
//    		return mapper.get(companyId).get(name);
//    	else
//    		return name;
    }
    
    public void setRealName(String name,String realName, String companyId) {
    	if(Utils.isNull(name)) return;
//    	mapper.put(name, realName);
        if(!mapper.containsKey(companyId))
            mapper.put(companyId,new ConcurrentHashMap<String,String>());
        mapper.get(companyId).put(name, realName);
    }
    
    private void loadMapperCfg(String companyId){
    	BufferedReader reader = null;
    	
    	try {
    		String path = System.getProperty("user.dir");
    		reader = new BufferedReader(new FileReader(new File(path + "\\"+ companyId +"\\static\\mapperConfig.txt")));
    		String temp = null;
    		String[] arr = null;
    		while(null != (temp = reader.readLine())) {
    			if("".equals(temp) || temp.indexOf("//") == 1 || temp.indexOf("[") == 0) continue;
    			if(temp.indexOf("=") == -1) {
    				temp = temp.trim();
    				setRealName(temp, temp, companyId);
    			}else {
    				arr = temp.split("=");
    				setRealName(arr[0].trim(), arr[1].trim(), companyId);
    			}
    		}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return;
		}finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }

    
    public int getAnID(String sname, String companyId) {
        AnO ano = getAnO(sname, companyId);
        if (null == ano)
            return -1;
        return ano.getId();
    }

    public int getStID(String sname, String companyId) {
        StO sto = getStO(sname, companyId);
        if (null == sto)
            return -1;
        return sto.getId();
    }

    public int getAcID(String sname, String companyId) {
        AcO aco = getAcO(sname, companyId);
        if (null == aco)
            return -1;
        return aco.getId();
    }

    public AnO getAnO(String sname, String companyId) {

        if (anMap.containsKey(companyId) && anMap.get(companyId).containsKey(sname))
            return anMap.get(companyId).get(sname);
        return null;
    }

    public StO getStO(String sname, String companyId) {
        if (stMap.containsKey(companyId) && stMap.get(companyId).containsKey(sname))
            return stMap.get(companyId).get(sname);
        return null;
    }

    public AcO getAcO(String sname, String companyId) {
        if (acMap.get(companyId).containsKey(sname) && acMap.containsKey(companyId))
            return acMap.get(companyId).get(sname);
        return null;
    }

    public StO getStO(int id, String companyId) {
        if (stIdNameMap.get(companyId).containsKey(id) && stIdNameMap.containsKey(companyId))
            return getStO(stIdNameMap.get(companyId).get(id), companyId);
        return null;
    }

    public AnO getAnO(int id, String companyId) {
        if (anIdNameMap.get(companyId).containsKey(id) && anIdNameMap.containsKey(companyId))
            return getAnO(anIdNameMap.get(companyId).get(id), companyId);
        return null;
    }

    public AcO getAcO(int id, String companyId) {
        if (acIdNameMap.get(companyId).containsKey(id) && acIdNameMap.containsKey(companyId))
            return getAcO(acIdNameMap.get(companyId).get(id), companyId);
        return null;
    }

    public List<AnO> getAnOByUnitNo(short unitNo, String companyId) {
        if (unitAnMap.containsKey(companyId) && unitAnMap.get(companyId).containsKey(unitNo))
            return unitAnMap.get(companyId).get(unitNo);
        return null;
    }

    public List<StO> getStOByUnitNo(short unitNo, String companyId) {
        if (unitStMap.containsKey(companyId) && unitStMap.get(companyId).containsKey(unitNo))
            return unitStMap.get(companyId).get(unitNo);
        return null;
    }

    public List<AcO> getAcOByUnitNo(short unitNo, String companyId) {
        if (unitAcMap.containsKey(companyId) && unitAcMap.get(companyId).containsKey(unitNo))
            return unitAcMap.get(companyId).get(unitNo);
        return null;
    }

    public List<UnitInfo> getAllUnitInfo(String companyId) {
        //return unitList;
        return unitList.get(companyId);
    }

    public Integer[] getAllAnId() {
        return anIdNameMap.keySet().toArray(new Integer[anIdNameMap.size()]);
    }

    public Integer[] getAllAcId() {
        return acIdNameMap.keySet().toArray(new Integer[acIdNameMap.size()]);
    }

    public Integer[] getAllStId() {
        return stIdNameMap.keySet().toArray(new Integer[stIdNameMap.size()]);
    }


    public CfgData() {
    }

    private static String getUserFilePath(String username) {
//        String rootPath = System.getProperty("user.dir") + "\\userlist";
//        File file = new File(rootPath);
//        File[] fileArray = file.listFiles();
//        String[] usernameArray = new String[fileArray.length];
//        File file1 = null;
//        String filePath = null;
//
//        for (int i=0; i<fileArray.length; i++) {
//            file1 = new File(fileArray[i].toString().trim());
//            String fileName = file1.getName();
//            usernameArray[i] = fileName;
//        }
//
//        for (int j=0; j<usernameArray.length; j++) {
//            if (username.equals(usernameArray[j])) {
//                filePath = fileArray[j].toString();
//            }
//        }
//
//        return filePath;
        return System.getProperty("user.dir") + "\\" + username + "\\unitConfigs";
    }


    //获取local.xml中所有的userName
    private static String[] usernameArray() {
        File file = null;
        List<String> list = new ArrayList<String>();

        //取得文件的根目录
        String rootPath = System.getProperty("user.dir");
        file = new File(rootPath + "\\local.xml");
        if (file != null && !file.exists()) {
            System.out.println(file + " file not found!");
        }

        SAXReader reader = new SAXReader();
        Document doc = null;
        try {
            doc = reader.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        Element root = doc.getRootElement();
        for (Object item : root.elements()) {
            if ("userName".equals(((Element) item).getName())) {
                list.add(((Element) item).getText());
            }
        }
        String[] temp = new String[list.size()];
        list.toArray(temp);

        return temp;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        anMap = new HashMap<String, Map<String, AnO>>();
        stMap = new HashMap<String, Map<String, StO>>();
        acMap = new HashMap<String, Map<String, AcO>>();
        anIdNameMap = new HashMap<String, Map<Integer, String>>();
        acIdNameMap = new HashMap<String, Map<Integer, String>>();
        stIdNameMap = new HashMap<String, Map<Integer, String>>();
        //unitList = new ArrayList<>();
        unitList = new HashMap<String, List<UnitInfo>>();
        unitAnMap = new HashMap<String, Map<Short, List<AnO>>>();
        unitAcMap = new HashMap<String, Map<Short, List<AcO>>>();
        unitStMap = new HashMap<String, Map<Short, List<StO>>>();



//			File file = new File(Utils._RESOURCES_PATH_ + Constants._DIR_UNITCFG_);

        //加载不同用户的配置文件
        File file = null;


        String[] userArray = usernameArray();

        for (int i=0; i<userArray.length; i++) {
            //加载3d系统字段映射文件
            loadMapperCfg(userArray[i]);

            String rootPath = getUserFilePath(userArray[i]);
            try {
                file = ResourceUtils.getFile(rootPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (file != null && !file.exists()) {
                logger.error(file + " file not found");
                return;
            }
            if (!file.isDirectory()) {
                logger.error(file + " file not directory");
            }

            unitAnMap.put(userArray[i],new HashMap<Short,List<AnO>>());
            unitAcMap.put(userArray[i],new HashMap<Short,List<AcO>>());
            unitStMap.put(userArray[i],new HashMap<Short,List<StO>>());
            unitList.put(userArray[i], new ArrayList<>());

            SAXReader reader = new SAXReader();
            File[] fileArr = file.listFiles();
            for (File item : fileArr) {
                Document doc = reader.read(item);
                Element root = doc.getRootElement();

                String temp = root.attributeValue("unitNo");
                Short unitNo = new Short(temp);

                temp = root.attributeValue("type");
                float type = new Float(temp);

                String name = root.attributeValue("name");

                UnitInfo ui = new UnitInfo(name, unitNo, (byte)type);

                unitAnMap.get(userArray[i]).put(unitNo, initAnO(doc, unitNo, userArray[i]));
                unitAcMap.get(userArray[i]).put(unitNo, initAcO(doc, unitNo, userArray[i]));
                unitStMap.get(userArray[i]).put(unitNo, initStO(doc, unitNo, userArray[i]));


                unitList.get(userArray[i]).add(ui);
            }
        }


        // 取得根目录路径
//        String rootPath = System.getProperty("user.dir");
//        try {
//            file = ResourceUtils.getFile(rootPath + "/unitConfigs");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        if (file != null && !file.exists()) {
//            logger.error(file + " file not found!");
//            return;
//        }
//        if (!file.isDirectory()) {
//            logger.error(file + " file not directory!");
//            return;
//        }
//        SAXReader reader = new SAXReader();
//        File[] fileArr = file.listFiles();
//        for (File item : fileArr) {
//            Document doc = reader.read(item);
//            Element root = doc.getRootElement();
//
//            String temp = root.attributeValue("unitNo");
//            Short unitNo = new Short(temp);
//
//            temp = root.attributeValue("type");
//            float type = new Float(temp);
//
//            String name = root.attributeValue("name");
//
//            UnitInfo ui = new UnitInfo(name, unitNo, (byte) type);
//
//            unitAnMap.put(unitNo, initAnO(doc, unitNo));
//            unitAcMap.put(unitNo, initAcO(doc, unitNo));
//            unitStMap.put(unitNo, initStO(doc, unitNo));
//
//            unitList.add(ui);
//        }

    }

    @SuppressWarnings("unchecked")
    /**
     * 初始化遥测
     */
    private List<AnO> initAnO(Document doc, short unitNo, String companyId) {
        List<AnO> anoList = new ArrayList<>();
        Element root;
        String query = "//dynamic/configs[@name='遥测']/config";
        List<Element> list = doc.selectNodes(query);

        for (Element e : list) {
            AnO ano = new AnO();
            ano.setUnitNo(unitNo);

            String ptNo = e.attributeValue("ptNo");
            ano.setPtNo(new Short(ptNo));

            for (Iterator<Element> it = e.elementIterator(); it.hasNext(); ) {
                root = it.next();
                String rootName = root.getName().toLowerCase();

                try {
                    if ("ename".equals(rootName)) {
                        if (root.getText() != null) {// 点名,系统英文名
                            ano.setSname(root.getText().trim());
                        }
                    } else if ("cname".equals(rootName)) {
                        if (root.getText() != null) {// 中文描述
                            ano.setCname(root.getText().trim());
                        }
                    } else if ("refv".equals(rootName)) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// 参考值
                            if (!text.isEmpty())
                                ano.setRefV(new Float(text.trim()));
                        }
                    } else if ("lgname".equals(rootName)) {// 单位
                        if (root.getText() != null) {
                            ano.setLgName(root.getText().trim());
                        }
                    } else if ("mask".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// 后台使能
                            if (!text.isEmpty()) {
                                float temp = new Float(text.trim());
                                ano.setMask((byte) temp);
                            }
                        }
                    } else if ("alarm".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// 报警
                            if (!text.isEmpty()) {
                                float temp = new Float(text.trim());
                                ano.setAlarm((byte) temp);
                            }
                        }
                    } else if ("poinum".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// 保留小数位数
                            if (!text.isEmpty()) {
                                float temp = new Float(text.trim());
                                ano.setPoinum((byte) temp);
                            }
                        }
                    } else if ("fi".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// 比例
                            if (!text.isEmpty())
                                ano.setFi(new Float(text.trim()));

                        }
                    } else if ("librank".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// 库级别
                            if (!text.isEmpty()) {
                                float temp = new Float(text.trim());
                                ano.setLibrank((byte) temp);
                            }
                        }
                    } else if ("zerov".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// 零区
                            if (!text.isEmpty())
                                ano.setZeroV(new Float(text.trim()));
                        }
                    } else if ("offsetv".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// 零偏
                            if (!text.isEmpty())
                                ano.setOffsetV(new Float(text.trim()));
                        }
                    } else if ("upv".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// 上限
                            if (!text.isEmpty())
                                ano.setUpV(new Float(text.trim()));
                        }
                    } else if ("dwv".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// 下限
                            if (!text.isEmpty())
                                ano.setDwV(new Float(text.trim()));
                        }
                    } else if ("uupv".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// 上上限
                            if (!text.isEmpty())
                                ano.setUupV(new Float(text.trim()));
                        }
                    } else if ("ddwv".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// 下下限
                            if (!text.isEmpty())
                                ano.setDdwV(new Float(text.trim()));
                        }
                    } else if ("type".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// 类型
                            if (!text.isEmpty()) {
                                float temp = new Float(text.trim());
                                ano.setType((byte) temp);
                            }
                        }
                    } else if ("nominus".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// 无负数
                            if (!text.isEmpty()) {
                                float temp = new Float(text.trim());
                                ano.setNominus((byte) temp);
                            }
                        }
                    }

                } catch (Exception e1) {
                    System.out.println(ano.getSname() + "初始化失败,"
                            + e1.getMessage());
                }
            }
            ano.setId(utils.getId(Constants.IDAN, ano.getUnitNo(),
                    ano.getPtNo()));
            //anMap.put(ano.getSname(), ano);
            if(null == anMap.get(companyId))
                anMap.put(companyId,new HashMap<String,AnO>());

            anMap.get(companyId).put(ano.getSname(), ano);
            //anIdNameMap.put(ano.getId(), ano.getSname());

            if (null == anIdNameMap.get(companyId)) {
                anIdNameMap.put(companyId, new HashMap<Integer, String>());
            }

            anIdNameMap.get(companyId).put(ano.getId(), ano.getSname());
            anoList.add(ano);
        }
        return anoList;
    }


    @SuppressWarnings("unchecked")
    /**
     * 初始化遥信
     */
    private List<StO> initStO(Document doc, short unitNo, String companyId) {
        List<StO> stoList = new ArrayList<>();
        Element root;
        String query = "//dynamic/configs[@name='遥信']/config";
        List<Element> list = doc.selectNodes(query);
        int max = -1;
        for (Element e : list) {
            StO sto = new StO();
            sto.setUnitNo(unitNo);

            String ptNo = e.attributeValue("ptNo");
            sto.setPtNo(new Short(ptNo));
            if (sto.getPtNo() > max)
                max = sto.getPtNo();
            for (Iterator<Element> it = e.elementIterator(); it.hasNext(); ) {
                root = it.next();
                String rootName = root.getName().toLowerCase();
                if ("ename".equals(rootName)) {
                    if (root.getText() != null) {
                        sto.setSname(root.getText());
                    }
                } else if ("cname".equals(rootName)) {
                    if (root.getText() != null) {
                        sto.setCname(root.getText());
                    }
                } else if ("mask".equals(rootName) && !root.getText().isEmpty()) {
                    if (root.getText() != null) {
                        String text = pattern.matcher(root.getText())
                                .replaceAll("");// 使能
                        if (!text.isEmpty()) {
                            float temp = new Float(text);
                            sto.setMask((byte) temp);
                        }
                    }
                } else if ("swidef".equals(rootName)
                        && !root.getText().isEmpty()) {
                    if (root.getText() != null) {
                        String text = pattern.matcher(root.getText())
                                .replaceAll("");// 开合定义
                        if (!text.isEmpty()) {
                            float temp = new Float(text);
                            sto.setSwidef((byte) temp);
                        }
                    }
                } else if ("type".equals(rootName) && !root.getText().isEmpty()) {
                    if (root.getText() != null) {
                        String text = pattern.matcher(root.getText())
                                .replaceAll("");// 类型
                        if (!text.isEmpty()) {
                            float temp = new Float(text);
                            sto.setType((byte) temp);
                        }
                    }
                } else if ("pcepd".equals(rootName)
                        && !root.getText().isEmpty()) {
                    if (root.getText() != null) {
                        String text = pattern.matcher(root.getText())
                                .replaceAll("");// EPD
                        if (!text.isEmpty()) {
                            float temp = new Float(text);
                            sto.setEpd((byte) temp);
                        }
                    }
                } else if ("soe".equals(rootName) && !root.getText().isEmpty()) {
                    if (root.getText() != null) {
                        String text = pattern.matcher(root.getText())
                                .replaceAll("");// SOE
                        if (!text.isEmpty()) {
                            float temp = new Float(text);
                            sto.setSoe((byte) temp);
                        }
                    }
                } else if ("alarm".equals(rootName)
                        && !root.getText().isEmpty()) {
                    if (root.getText() != null) {
                        String text = pattern.matcher(root.getText())
                                .replaceAll("");// 报警
                        if (!text.isEmpty()) {
                            float temp = new Float(text);
                            sto.setAlarm((byte) temp);
                        }
                    }
                }
            }
            sto.setId(utils.getId(Constants.IDST, sto.getUnitNo(),
                    sto.getPtNo()));
            //stMap.put(sto.getSname(), sto);

            if (null == stMap.get(companyId)) {
                stMap.put(companyId, new HashMap<String, StO>());
            }

            stMap.get(companyId).put(sto.getSname(), sto);
            //stIdNameMap.put(sto.getId(), sto.getSname());

            if (null == stIdNameMap.get(companyId)) {
                stIdNameMap.put(companyId, new HashMap<Integer, String>());
            }

            stIdNameMap.get(companyId).put(sto.getId(), sto.getSname());
            stoList.add(sto);
        }
        return stoList;
    }


    @SuppressWarnings("unchecked")
    /**
     * 初始化电度
     */
    private List<AcO> initAcO(Document doc, short unitNo, String companyId) {
        List<AcO> acoList = new ArrayList<>();
        Element root;
        String query = "//dynamic/configs[@name='电度']/config";
        List<Element> list = doc.selectNodes(query);
        int max = -1;
        for (Element e : list) {
            AcO aco = new AcO();
            aco.setUnitNo(unitNo);

            String ptNo = e.attributeValue("ptNo");
            aco.setPtNo(new Short(ptNo));
            if (aco.getPtNo() > max)
                max = aco.getPtNo();
            for (Iterator<Element> it = e.elementIterator(); it.hasNext(); ) {
                root = it.next();
                String rootName = root.getName().toLowerCase();
                if ("ename".equals(rootName)) {
                    if (root.getText() != null) {
                        aco.setSname(root.getText());
                    }
                } else if ("cname".equals(rootName)) {
                    if (root.getText() != null) {
                        aco.setCname(root.getText());
                    }
                } else if ("mask".equals(rootName)) {
                    if (root.getText() != null && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");
                            if (!text.isEmpty()) {
                                float temp = new Float(text);
                                aco.setMask((byte) temp);
                            }
                        }
                    }
                } else if ("poinum".equals(rootName)
                        && !root.getText().isEmpty()) {
                    if (root.getText() != null) {
                        String text = pattern.matcher(root.getText())
                                .replaceAll("");
                        if (!text.isEmpty()) {
                            float temp = new Float(text);
                            aco.setPoinum((byte) temp);
                        }
                    }
                } else if ("fi".equals(rootName) && !root.getText().isEmpty()) {
                    if (root.getText() != null) {
                        String text = pattern.matcher(root.getText())
                                .replaceAll("");
                        if (!text.isEmpty())
                            aco.setFi(new Float(text));
                    }
                } else if ("inlib".equals(rootName)
                        && !root.getText().isEmpty()) {
                    if (root.getText() != null) {
                        String text = pattern.matcher(root.getText())
                                .replaceAll("");
                        if (!text.isEmpty()) {
                            float temp = new Float(text);
                            aco.setLibrank((byte) temp);
                        }
                    }
                } else if ("full".equals(rootName) && !root.getText().isEmpty()) {
                    if (root.getText() != null) {
                        String text = pattern.matcher(root.getText())
                                .replaceAll("");
                        if (!text.isEmpty()) {
                            double temp = new Double(text);
                            aco.setFullV((long) temp);
                        }
                    }
                } else if ("lgname".equals(rootName)) {
                    if (root.getText() != null) {
                        aco.setLgName(root.getText());
                    }
                }

            }
            aco.setId(utils.getId(Constants.IDACC, aco.getUnitNo(),
                    aco.getPtNo()));
            //acMap.put(aco.getSname(), aco);

            if (null == acMap.get(companyId)) {
                acMap.put(companyId, new HashMap<String, AcO>());
            }

            acMap.get(companyId).put(aco.getSname(), aco);
            //acIdNameMap.put(aco.getId(), aco.getSname());

            if (null == acIdNameMap.get(companyId)) {
                acIdNameMap.put(companyId, new HashMap<Integer, String>());
            }

            acIdNameMap.get(companyId).put(aco.getId(), aco.getSname());
            acoList.add(aco);
        }
        return acoList;
    }
}
