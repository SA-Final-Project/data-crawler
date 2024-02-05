package edu.miu.kafka_ui;

import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import java.time.Duration;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// @RestController
public class Controller {

    @Data
    class Message {
        String topic;
        String msg;
    }

    @Autowired
    KafkaTemplate kafkaTemplate;

    @PostMapping("/")
    public ResponseEntity<?> sendMsg(@RequestBody Message msg) {

        kafkaTemplate.send(msg.topic, msg.msg);
        return ResponseEntity.ok().build();
    }

}
