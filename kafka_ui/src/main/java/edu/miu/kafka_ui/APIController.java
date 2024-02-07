package edu.miu.kafka_ui;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController("/api")
public class APIController {

    @GetMapping("/")
    public ResponseEntity<?> getAPIData() {
        return new SomeData();
    }

}
