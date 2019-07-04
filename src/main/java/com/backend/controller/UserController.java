package com.backend.controller;


import com.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/login")
    public boolean login(@RequestBody Map<String, String> map) {

//        while(true) {
//            if (!userService.login(map.get("username"), map.get("password"), map.get("companyId")))
//                break;
//            try {
//                Thread.sleep(1500);
//            } catch (Exception ex) {
//            }
//        }
//
//        return false;

        return userService.login(map.get("userName"), map.get("password"), map.get("companyId"));
    }


}
