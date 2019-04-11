package com.backend.controller;

import com.backend.service.PipeInfoService;
import com.backend.util.CfgData;
import com.backend.util.Constants;
import com.backend.vo.PipeInfo;
import com.backend.vo.PipeMaintRecord;

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
    public PipeInfo getPipeInfo(@RequestBody Map<String,Object> map,@RequestBody String companyId) throws Exception{
    	byte pipeType = Constants.PIPE_STRONGCABLE;
    	String ename = null;
    	if(map.containsKey("pipeType")) {
    		pipeType = Byte.parseByte(map.get("pipeType").toString());
    	}
    	ename = map.get("ename").toString();
    	ename = cfgData.getRealName(ename);
    	
    	System.out.println("pipeType:" + pipeType + ",ename:" + ename);
    	
    	return pipeInfoService.getPipeInfo(pipeType, ename, companyId);
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getPipeInfos", consumes = "application/json")
	public List<PipeInfo> getPipeInfos(@RequestBody Map<String, Object> map,@RequestBody String companyId) throws Exception{
    	byte pipeType = Constants.PIPE_STRONGCABLE;
    	List<String> enames = null;
    	
    	if(map.containsKey("pipeType")) {
    		pipeType = Byte.parseByte(map.get("pipeType").toString());
    	}
    	
    	if(map.get("enames") != null) {
    		enames = (List<String>)map.get("enames");
    	}
    	
    	System.out.println("pipeType:" + pipeType + ",enames:" + enames);
    	
    	return pipeInfoService.getPipeInfos(pipeType, enames, companyId);
    }
    
    @RequestMapping(value="/getPipeMaintRecord",consumes="application/json")
    public List<PipeMaintRecord> getPipeMaintRecords(@RequestBody Map<String, Object> map,@RequestBody String companyId) throws Exception{
    	byte pipeType = Constants.PIPE_STRONGCABLE;
    	String pipeId = null;
    	if(map.containsKey("pipeType")) {
    		pipeType = Byte.parseByte(map.get("pipeType").toString());
    	}
    	pipeId = map.get("pipeId").toString();
    	
    	System.out.println("pipeType:" + pipeType + ",pipeId:" + pipeId);
    	
    	return pipeInfoService.getPipeMaintRecords(pipeType, pipeId, companyId);
    }
}
