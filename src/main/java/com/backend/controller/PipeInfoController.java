package com.backend.controller;

import com.backend.service.PipeInfoService;
import com.backend.util.CfgData;
import com.backend.util.Constants;
import com.backend.vo.PipeInfo;
import com.backend.vo.PipeMaintRecord;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pipeInfo")
public class PipeInfoController {
	@Autowired
	private CfgData cfgData;
	
    private PipeInfoService pipeInfoService;

    @Autowired
    public PipeInfoController(PipeInfoService pipeInfoService){
        this.pipeInfoService = pipeInfoService;
    }

    @RequestMapping(value = "/getPipeInfo", consumes = "application/json")
    public PipeInfo getPipeInfo(@RequestBody Map<String,Object> map) throws Exception{
    	byte pipeType = Constants.PIPE_STRONGCABLE;
    	String ename = null;
    	if(map.containsKey("pipeType")) {
    		pipeType = Byte.parseByte(map.get("pipeType").toString());
    	}
    	ename = map.get("ename").toString();
    	ename = cfgData.getRealName(ename, map.get("companyId").toString());
    	
    	System.out.println("pipeType : " + pipeType + ", ename : " + ename);
    	
    	return pipeInfoService.getPipeInfo(pipeType, ename, map.get("companyId").toString());
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getPipeInfos", consumes = "application/json")
	public List<PipeInfo> getPipeInfos(@RequestBody Map<String, String> map) throws Exception{

    	/**
		 * {
		 *     "enames" :"[\"pipe01\",\"pipe02\"]",
		 *     "companyId" : "demo"
		 * }
		 * */

    	byte pipeType = Constants.PIPE_STRONGCABLE;
    	List<String> enames = null;

    	Gson gson = new Gson();

    	if(map.containsKey("pipeType")) {
    		pipeType = Byte.parseByte(map.get("pipeType").toString());
    	}
    	
    	if(map.get("enames") != null) {
    		enames = gson.fromJson(map.get("enames"), new TypeToken<List<String>>() {
			}.getType());
    		//enames = (List<String>)map.get("enames");
    	}
    	
    	System.out.println("pipeType:" + pipeType + ",enames:" + enames);
    	
    	return pipeInfoService.getPipeInfos(pipeType, enames, map.get("companyId").toString());
    }
    
    @RequestMapping(value="/getPipeMaintRecord",consumes="application/json")
    public List<PipeMaintRecord> getPipeMaintRecords(@RequestBody Map<String, Object> map) throws Exception{

    	/**
		 * {
		 *     "pipeId" : "7",
		 *     "companyId" : "demo"
		 * }
		 * */

    	byte pipeType = Constants.PIPE_STRONGCABLE;
    	String pipeId = null;
    	if(map.containsKey("pipeType")) {
    		pipeType = Byte.parseByte(map.get("pipeType").toString());
    	}
    	pipeId = map.get("pipeId").toString();
    	
    	System.out.println("pipeType:" + pipeType + ",pipeId:" + pipeId);
    	
    	return pipeInfoService.getPipeMaintRecords(pipeType, pipeId, map.get("companyId").toString());
    }
}
