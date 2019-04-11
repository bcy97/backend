package com.backend.service;

import com.backend.vo.PipeMaintRecord;

public interface PipeMaintRecordService {

    PipeMaintRecord[] getAllPipeMaintRecord(String companyId);

    PipeMaintRecord[] getPipeMaintRecordsById(String id, String companyId);

    PipeMaintRecord[] getPipeMaintRecordsByPipeId(String pipeId, String companyId);
}
