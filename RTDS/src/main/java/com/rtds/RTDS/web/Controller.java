package com.rtds.RTDS.web;

import com.rtds.RTDS.domain.RTDIS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    RTDIS rtdis;

    @GetMapping()
    public String message(){
        return "No API URL provided. Please provide an API URL as a command line argument.";

    }
}
