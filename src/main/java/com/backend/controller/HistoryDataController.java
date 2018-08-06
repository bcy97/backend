package com.backend.controller;


import com.backend.service.HistoryDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/histroy")
public class HistoryDataController {

    private HistoryDataService historyDataService;

    @Autowired
    public HistoryDataController(HistoryDataService historyDataService) {
        this.historyDataService = historyDataService;
    }

    @RequestMapping("/list")
    public List<String> getHistoryDataList() {

        return historyDataService.getDataList();

    }


}
