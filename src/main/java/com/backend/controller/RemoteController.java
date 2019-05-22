package com.backend.controller;

import com.backend.service.RemoteService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/remote")
public class RemoteController {
    private RemoteService remoteService;

    @Autowired
    public RemoteController(RemoteService remoteService){
        this.remoteService = remoteService;
    }

    /***
     * @param data
     *
     * */
    @RequestMapping(value = "/remoteControl", consumes = "application/json")
    public void remoteControl(@RequestBody Map<String, String> data){

        String companyId = data.get("companyId");

        System.out.println("remote");

        Gson gson = new Gson();
        String[] ptNames = gson.fromJson(data.get("ptNames"), new TypeToken<String[]>() {
        }.getType());

        remoteService.remoteControl(ptNames,new Byte(data.get("state")), companyId);
    }
}
