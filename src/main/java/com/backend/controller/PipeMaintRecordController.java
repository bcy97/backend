package com.backend.controller;

import com.backend.service.PipeMaintRecordService;
import com.backend.service.impl.PipeMaintRecordServiceImpl;
import com.backend.vo.PipeMaintRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/pipeMaintRecord")
public class PipeMaintRecordController {

    private PipeMaintRecordService pipeMaintRecordService;

    @Autowired
    public PipeMaintRecordController(PipeMaintRecordService pipeMaintRecordService){
        this.pipeMaintRecordService = pipeMaintRecordService;
    }

    @RequestMapping(value = "/getPipeMaintRecord", consumes = "application/json")
    public PipeMaintRecord[] getPipeMaintRecord(@RequestBody Map<String,String> data,@RequestBody String companyId){
        if(null != data && data.containsKey("id"))
            return pipeMaintRecordService.getPipeMaintRecordsById(data.get("id"), companyId);
        else if(null != data && data.containsKey("pipeId"))
            return pipeMaintRecordService.getPipeMaintRecordsByPipeId(data.get("pipeId"), companyId);
        else
            return pipeMaintRecordService.getAllPipeMaintRecord(companyId);
    }

}
