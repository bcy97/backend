package com.backend.util;

import com.backend.vo.AcO;
import com.backend.vo.AnO;
import com.backend.vo.StO;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class CfgData implements ApplicationRunner {
    Logger logger = Logger.getLogger("CfgData");
    private Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
    private Map<String, AnO> anMap = null;
    private Map<String, StO> stMap = null;
    private Map<String, AcO> acMap = null;
    private Map<Integer, String> anIdNameMap = null;
    private Map<Integer, String> acIdNameMap = null;
    private Map<Integer, String> stIdNameMap = null;


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

//			File file = new File(Utils._RESOURCES_PATH_ + Constants._DIR_UNITCFG_);
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:unitCfg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (!file.exists()) {
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

            initAnO(doc, unitNo);
            initAcO(doc, unitNo);
            initStO(doc, unitNo);

        }

    }

    @SuppressWarnings("unchecked")
    /**
     * 初始化遥测
     */
    private void initAnO(Document doc, short unitNo) {
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
                        if (root.getText() != null) {
                            ano.setSname(root.getText().trim());
                        }
                    } else if ("cname".equals(rootName)) {
                        if (root.getText() != null) {
                            ano.setCname(root.getText().trim());
                        }
                    } else if ("refv".equals(rootName)) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                            if (!text.isEmpty())
                                ano.setRefV(new Float(text.trim()));
                        }
                    } else if ("lgname".equals(rootName)) {
                        if (root.getText() != null) {
                            ano.setLgName(root.getText().trim());
                        }
                    } else if ("mask".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                            if (!text.isEmpty()) {
                                float temp = new Float(text.trim());
                                ano.setMask((byte) temp);
                            }
                        }
                    } else if ("alarm".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                            if (!text.isEmpty()) {
                                float temp = new Float(text.trim());
                                ano.setAlarm((byte) temp);
                            }
                        }
                    } else if ("poinum".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                            if (!text.isEmpty()) {
                                float temp = new Float(text.trim());
                                ano.setPoinum((byte) temp);
                            }
                        }
                    } else if ("fi".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                            if (!text.isEmpty())
                                ano.setFi(new Float(text.trim()));

                        }
                    } else if ("librank".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                            if (!text.isEmpty()) {
                                float temp = new Float(text.trim());
                                ano.setLibrank((byte) temp);
                            }
                        }
                    } else if ("zerov".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                            if (!text.isEmpty())
                                ano.setZeroV(new Float(text.trim()));
                        }
                    } else if ("offsetv".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                            if (!text.isEmpty())
                                ano.setOffsetV(new Float(text.trim()));
                        }
                    } else if ("upv".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                            if (!text.isEmpty())
                                ano.setUpV(new Float(text.trim()));
                        }
                    } else if ("dwv".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                            if (!text.isEmpty())
                                ano.setDwV(new Float(text.trim()));
                        }
                    } else if ("uupv".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                            if (!text.isEmpty())
                                ano.setUupV(new Float(text.trim()));
                        }
                    } else if ("ddwv".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                            if (!text.isEmpty())
                                ano.setDdwV(new Float(text.trim()));
                        }
                    } else if ("type".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                            if (!text.isEmpty()) {
                                float temp = new Float(text.trim());
                                ano.setType((byte) temp);
                            }
                        }
                    } else if ("nominus".equals(rootName)
                            && !root.getText().isEmpty()) {
                        if (root.getText() != null) {
                            String text = pattern.matcher(root.getText())
                                    .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                            if (!text.isEmpty()) {
                                float temp = new Float(text.trim());
                                ano.setNominus((byte) temp);
                            }
                        }
                    }

                } catch (Exception e1) {
                    System.out.println(ano.getSname() + " è§£æžå‡ºé”™,"
                            + e1.getMessage());
                }
            }
            ano.setId(Utils.getId(Constants.IDAN, ano.getUnitNo(),
                    ano.getPtNo()));
            anMap.put(ano.getSname(), ano);
            anIdNameMap.put(ano.getId(), ano.getSname());
        }

    }


    @SuppressWarnings("unchecked")
    /**
     * 初始化遥信
     */
    private void initStO(Document doc, short unitNo) {
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
                                .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                        if (!text.isEmpty()) {
                            float temp = new Float(text);
                            sto.setMask((byte) temp);
                        }
                    }
                } else if ("swidef".equals(rootName)
                        && !root.getText().isEmpty()) {
                    if (root.getText() != null) {
                        String text = pattern.matcher(root.getText())
                                .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                        if (!text.isEmpty()) {
                            float temp = new Float(text);
                            sto.setSwidef((byte) temp);
                        }
                    }
                } else if ("type".equals(rootName) && !root.getText().isEmpty()) {
                    if (root.getText() != null) {
                        String text = pattern.matcher(root.getText())
                                .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                        if (!text.isEmpty()) {
                            float temp = new Float(text);
                            sto.setType((byte) temp);
                        }
                    }
                } else if ("pcepd".equals(rootName)
                        && !root.getText().isEmpty()) {
                    if (root.getText() != null) {
                        String text = pattern.matcher(root.getText())
                                .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                        if (!text.isEmpty()) {
                            float temp = new Float(text);
                            sto.setEpd((byte) temp);
                        }
                    }
                } else if ("soe".equals(rootName) && !root.getText().isEmpty()) {
                    if (root.getText() != null) {
                        String text = pattern.matcher(root.getText())
                                .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                        if (!text.isEmpty()) {
                            float temp = new Float(text);
                            sto.setSoe((byte) temp);
                        }
                    }
                } else if ("alarm".equals(rootName)
                        && !root.getText().isEmpty()) {
                    if (root.getText() != null) {
                        String text = pattern.matcher(root.getText())
                                .replaceAll("");// åŽ»æŽ‰ \t \n ç©ºæ ¼
                        if (!text.isEmpty()) {
                            float temp = new Float(text);
                            sto.setAlarm((byte) temp);
                        }
                    }
                }
            }
            sto.setId(Utils.getId(Constants.IDST, sto.getUnitNo(),
                    sto.getPtNo()));
            stMap.put(sto.getSname(), sto);
            stIdNameMap.put(sto.getId(), sto.getSname());
        }
    }


    @SuppressWarnings("unchecked")
    /**
     * 初始化电度
     */
    private void initAcO(Document doc, short unitNo) {
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
            aco.setId(Utils.getId(Constants.IDACC, aco.getUnitNo(),
                    aco.getPtNo()));
            acMap.put(aco.getSname(), aco);
            acIdNameMap.put(aco.getId(), aco.getSname());
        }

    }
}
