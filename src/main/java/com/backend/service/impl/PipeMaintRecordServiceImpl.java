package com.backend.service.impl;

import com.backend.dao.PipeMaintRecordDao;
import com.backend.service.PipeMaintRecordService;
import com.backend.vo.PipeMaintRecord;
import org.springframework.stereotype.Service;

@Service
public class PipeMaintRecordServiceImpl implements PipeMaintRecordService {

    private PipeMaintRecordDao pipeMaintRecordDao;

    public PipeMaintRecordServiceImpl(PipeMaintRecordDao pipeMaintRecordDao){
        this.pipeMaintRecordDao = pipeMaintRecordDao;
    }

    @Override
    public PipeMaintRecord[] getAllPipeMaintRecord(String companyId) {
        return pipeMaintRecordDao.getPipeMaintRecords(null,null, companyId);
    }

    @Override
    public PipeMaintRecord[] getPipeMaintRecordsById(String id, String companyId) {
        return pipeMaintRecordDao.getPipeMaintRecords(id,null, companyId);
    }

    @Override
    public PipeMaintRecord[] getPipeMaintRecordsByPipeId(String pipeId, String companyId) {
        return pipeMaintRecordDao.getPipeMaintRecords(null,pipeId, companyId);
    }
}
