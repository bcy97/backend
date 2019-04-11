package com.backend.service.impl;

import com.backend.dao.PipeInfoDao;
import com.backend.service.PipeInfoService;
import com.backend.vo.PipeInfo;
import com.backend.vo.PipeMaintRecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PipeInfoServiceImpl implements PipeInfoService {

    private PipeInfoDao pipeInfoDao;

    @Autowired
    public PipeInfoServiceImpl(PipeInfoDao pipeInfoDao){
        this.pipeInfoDao = pipeInfoDao;
    }

	@Override
	public PipeInfo getPipeInfo(byte pipeType, String ename, String companyId) throws Exception {
		// TODO Auto-generated method stub
		return pipeInfoDao.getPipeInfo(pipeType, ename, companyId);
	}

	@Override
	public List<PipeInfo> getPipeInfos(byte pipeType, List<String> enames, String companyId) throws Exception {
		// TODO Auto-generated method stub
		return pipeInfoDao.getPipeInfos(pipeType, enames, companyId);
	}

	@Override
	public List<PipeMaintRecord> getPipeMaintRecords(byte pipeType, String pipeId, String companyId) throws Exception {
		// TODO Auto-generated method stub
		return pipeInfoDao.getPipeMaintRecords(pipeType,pipeId, companyId);
	}
}
