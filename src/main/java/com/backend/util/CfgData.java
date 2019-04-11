package com.backend.util;

import com.backend.vo.AcO;
import com.backend.vo.AnO;
import com.backend.vo.StO;
import com.backend.vo.UnitInfo;
import org.apache.log4j.Logger;
import org.dom4j.Document;
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
    private Map<String, AnO> anMap = null;
    private Map<String, StO> stMap = null;
    private Map<String, AcO> acMap = null;
    private Map<Integer, String> anIdNameMap = null;
    private Map<Integer, String> acIdNameMap = null;
    private Map<Integer, String> stIdNameMap = null;
    private List<UnitInfo> unitList = null;
    private Map<Short, List<AnO>> unitAnMap = null;
    private Map<Short, List<StO>> unitStMap = null;
    private Map<Short, List<AcO>> unitAcMap = null;

    private Map<String, String> mapper = new ConcurrentHashMap<>();
    
    public String getRealName(String name) {
    	if(Utils.isNull(name) || !mapper.containsKey(name))
    		return name;
    	else
    		return mapper.get(name);
    }
    
    public void setRealName(String name,String realName) {
    	if(Utils.isNull(name)) return;
    	mapper.put(name, realName);
    }
    
    private void loadMapperCfg(){
    	BufferedReader reader = null;
    	
    	try {
    		String path = System.getProperty("user.dir");
    		reader = new BufferedReader(new FileReader(new File(path + "/static/mapperConfig.txt")));
    		String temp = null;
    		String[] arr = null;
    		while(null != (temp = reader.readLine())) {
    			if("".equals(temp) || temp.indexOf("//") == 1 || temp.indexOf("[") == 0) continue;
    			if(temp.indexOf("=") == -1) {
    				temp = temp.trim();
    				setRealName(temp, temp);
    			}else {
    				arr = temp.split("=");
    				setRealName(arr[0].trim(), arr[1].trim());
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
    
    public int getAnID(String sname) {
        AnO ano = getAnO(sname);
        if (null == ano)
            return -1;
        return ano.getId();
    }

    public int getStID(String sname) {
        StO sto = getStO(sname);
        if (null == sto)
            return -1;
        return sto.getId();
    }

    public int getAcID(String sname) {
        AcO aco = getAcO(sname);
        if (null == aco)
            return -1;
        return aco.getId();
    }

    public AnO getAnO(String sname) {
        if (anMap.containsKey(sname))
            return anMap.get(sname);
        return null;
    }

    public StO getStO(String sname) {
        if (stMap.containsKey(sname))
            return stMap.get(sname);
        return null;
    }

    public AcO getAcO(String sname) {
        if (acMap.containsKey(sname))
            return acMap.get(sname);
        return null;
    }

    public StO getStO(int id) {
        if (stIdNameMap.containsKey(id))
            return getStO(stIdNameMap.get(id));
        return null;
    }

    public AnO getAnO(int id) {
        if (anIdNameMap.containsKey(id))
            return getAnO(anIdNameMap.get(id));
        return null;
    }

    public AcO getAcO(int id) {
        if (acIdNameMap.containsKey(id))
            return getAcO(acIdNameMap.get(id));
        return null;
    }

    public List<AnO> getAnOByUnitNo(short unitNo) {
        if (unitAnMap.containsKey(unitNo))
            return unitAnMap.get(unitNo);
        return null;
    }

    public List<StO> getStOByUnitNo(short unitNo) {
        if (unitStMap.containsKey(unitNo))
            return unitStMap.get(unitNo);
        return null;
    }

    public List<AcO> getAcOByUnitNo(short unitNo) {
        if (unitAcMap.containsKey(unitNo))
            return unitAcMap.get(unitNo);
        return null;
    }

    public List<UnitInfo> getAllUnitInfo() {
        return unitList;
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


    @Override
    public void run(ApplicationArguments args) throws Exception {
        anMap = new HashMap<>();
        stMap = new HashMap<>();
        acMap = new HashMap<>();
        anIdNameMap = new HashMap<>();
        acIdNameMap = new HashMap<>();
        stIdNameMap = new HashMap<>();
        unitList = new ArrayList<>();
        unitAnMap = new HashMap<>();
        unitAcMap = new HashMap<>();
        unitStMap = new HashMap<>();
        //加载3d系统字段映射文件
        loadMapperCfg();

//			File file = new File(Utils._RESOURCES_PATH_ + Constants._DIR_UNITCFG_);
        File file = null;
        // 取得根目录路径
        String rootPath = System.getProperty("user.dir");
        try {
            file = ResourceUtils.getFile(rootPath + "/unitConfigs");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (file != null && !file.exists()) {
            logger.error(file + " file not found!");
            return;
        }
        if (!file.isDirectory()) {
            logger.error(file + " file not directory!");
            return;
        }
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

            UnitInfo ui = new UnitInfo(name, unitNo, (byte) type);

            unitAnMap.put(unitNo, initAnO(doc, unitNo));
            unitAcMap.put(unitNo, initAcO(doc, unitNo));
            unitStMap.put(unitNo, initStO(doc, unitNo));

            unitList.add(ui);
        }

    }

    @SuppressWarnings("unchecked")
    /**
     * 初始化遥测
     */
    private List<AnO> initAnO(Document doc, short unitNo) {
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
            anMap.put(ano.getSname(), ano);
            anIdNameMap.put(ano.getId(), ano.getSname());
            anoList.add(ano);
        }
        return anoList;
    }


    @SuppressWarnings("unchecked")
    /**
     * 初始化遥信
     */
    private List<StO> initStO(Document doc, short unitNo) {
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
            stMap.put(sto.getSname(), sto);
            stIdNameMap.put(sto.getId(), sto.getSname());
            stoList.add(sto);
        }
        return stoList;
    }


    @SuppressWarnings("unchecked")
    /**
     * 初始化电度
     */
    private List<AcO> initAcO(Document doc, short unitNo) {
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
            acMap.put(aco.getSname(), aco);
            acIdNameMap.put(aco.getId(), aco.getSname());
            acoList.add(aco);
        }
        return acoList;
    }
}
