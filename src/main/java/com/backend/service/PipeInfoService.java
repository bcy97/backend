package com.backend.service;

import java.util.List;

import com.backend.vo.PipeInfo;
import com.backend.vo.PipeMaintRecord;

public interface PipeInfoService {
	PipeInfo getPipeInfo(byte pipeType,String ename, String companyId) throws Exception;
	List<PipeInfo> getPipeInfos(byte pipeType,List<String> enames, String companyId) throws Exception;
	List<PipeMaintRecord> getPipeMaintRecords(byte pipeType, String pipeId, String companyId) throws Exception;
}
