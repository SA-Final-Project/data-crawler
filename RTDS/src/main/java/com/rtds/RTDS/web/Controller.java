package com.rtds.RTDS.web;

import com.rtds.RTDS.domain.RTDIS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    RTDIS rtdis;

    @GetMapping("/check")
    public String check(){
        //rtdis.sendMessage();
        rtdis.startStreaming();
        return "Okay";

    }
}
