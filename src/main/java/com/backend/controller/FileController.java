package com.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;

@RestController
public class FileController {

    public File[] download(ArrayList<String> filename) {
        return null;
    }

    public boolean compare(String md5) {

        return false;
    }

}
