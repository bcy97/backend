package com.backend.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ReadINIFile {
	private final String sDefault = "_DEFAULT_";
	// 文件路径
	private String path = null;
	/***
	 * 是否有错误
	 * */
	public Boolean wrong = false;
	/***
	 * 错误信息
	 * */
	public String msg = null;
	/***
	 * 节点名和节点下的每一行的哈希表
	 * */
	private HashMap<String, List<String>> sectionMap = null;
	/***
	 *
	 * */
	private HashMap<String, LinkedHashMap<String, String>> sectionKeyValueMap = null;
	private String nowSection;

	/***
	 * 读ini文件
	 * */
	public Boolean readIniFile() {
		if (path != null && !path.replace(" ", "").isEmpty()) {
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(path));
			} catch (Exception e) {
				// TODO: handle exception
				wrong = true;
				msg = "读取ini文件失败";
				return !wrong;
			}
			try {
				String sLine = reader.readLine();
				while (sLine != null) {
					sLine = sLine.replace(" ", "");
					if (!sLine.isEmpty()) {
						parseLine(sLine);
					}

					sLine = reader.readLine();
				}
			} catch (Exception e) {
				wrong = true;
				msg = e.getMessage();
				return !wrong;
			}

		} else {
			wrong = true;
			msg = "读取ini文件失败";
		}
		return !wrong;
	}

	/***
	 * 解析每一行
	 * */
	private void parseLine(String line) {

		line = line.trim();
		// 如果是以双斜杠打头的说明是注释,跳过
		if (line.matches("^[//|#].*$")) {
			return;
		} else if (line.matches("^\\[.*\\]$")) {// [section]
			line = line.substring(1);
			line = line.substring(0, line.length() - 1);
			addSection(line);
		} else {// key=value || key
			addKeyValues(line);
		}
	}

	/***
	 * 添加节点
	 * */
	private void addSection(String section) {
		nowSection = section;
		List<String> keyValues = new ArrayList<>();
		synchronized (sectionMap) {
			sectionMap.put(nowSection, keyValues);
		}

		LinkedHashMap<String, String> keyValueMap = new LinkedHashMap<>();
		synchronized (sectionKeyValueMap) {
			sectionKeyValueMap.put(nowSection, keyValueMap);
		}
	}

	/***
	 * 添加节点下的键值
	 * */
	private void addKeyValues(String keyValue) {
		// 当前节点为空就用缺省节点
		if (nowSection == null || nowSection.isEmpty()) {
			nowSection = sDefault;
			List<String> keyValues = new ArrayList<>();
			sectionMap.put(nowSection, keyValues);

			LinkedHashMap<String, String> keyValueMap = new LinkedHashMap<>();
			sectionKeyValueMap.put(nowSection, keyValueMap);
		}
		String arr[] = keyValue.split("=");
		String key = arr[0];
		String value = "";
		if (arr.length > 1) {
			value = arr[1];
		}
		
		synchronized (sectionMap) {
			sectionMap.get(nowSection).add(keyValue);	
		}
		
		synchronized (sectionKeyValueMap) {
			sectionKeyValueMap.get(nowSection).put(key, value);	
		}
		
	}

	public List<String> getAllSections(){
		return new ArrayList<>(sectionMap.keySet());
	}
	
	public Boolean sectionContainsKey(String section, String key)
    {
        if (sectionKeyValueMap.containsKey(section))
            return sectionKeyValueMap.get(section).containsKey(key);

        return false;
    }
	
	public List<String> sectionGetKeys(String section){
		if(sectionKeyValueMap.containsKey(section))
			return new ArrayList<>(sectionKeyValueMap.get(section).keySet());
		return null;
	}
	
	/***
	 * 通过节点名和键找到值
	 * */
	public String getValue(String section,String key){
		if(section == null || section.isEmpty()){
			section = sDefault;
		}
		synchronized (sectionKeyValueMap) {
			if(sectionKeyValueMap.containsKey(section)){
				return sectionKeyValueMap.get(section).get(key);
			}else{
				return null;
			}
			
		}
	}

	/***
	 * 通过节点名获取该节点下的所有行
	 * */
	public List<String> getKeyValue(String section){
		if(section == null || section.isEmpty()){
			section = sDefault;
		}
		synchronized (sectionMap) {
			return sectionMap.get(section);
		}
	}
	
	/***
	 * 通过节点名和索引获取指定的行
	 * */
	public String getLine(String section,int index){
		List<String> list = getKeyValue(section);
		if(list != null && list.size() > index){
			return list.get(index);
		}
		return null;
	} 
	
	public ReadINIFile(String path) {
		this.path = path;
		sectionKeyValueMap = new HashMap<>();
		sectionMap = new HashMap<>();
	}

	public ReadINIFile() {
		sectionKeyValueMap = new HashMap<>();
		sectionMap = new HashMap<>();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
