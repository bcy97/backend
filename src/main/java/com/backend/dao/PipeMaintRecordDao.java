package com.backend.dao;

import com.backend.vo.PipeMaintRecord;

public interface PipeMaintRecordDao {
    PipeMaintRecord[] getPipeMaintRecords(String id,String pipeId, String companyId);
}
