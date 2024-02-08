package edu.miu.kafka_ui;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class APIController {

    @Autowired
    DataSource ds;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/")
    public String getIndexHtml() {
        return "index.html";
    }

    @GetMapping(value = "/api", produces = "application/json")
    @ResponseBody
    public String getAPIData() throws Exception {
        return objectMapper.writeValueAsString(ds.generateData());
    }

}
