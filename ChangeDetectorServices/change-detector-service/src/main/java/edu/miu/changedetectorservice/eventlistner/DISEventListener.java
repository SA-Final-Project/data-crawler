package edu.miu.changedetectorservice.eventlistner;

import edu.miu.changedetectorservice.service.StandardDeviationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class DISEventListener {
    private final StandardDeviationService service;

    @KafkaListener(topics = "${kafka.topics.dis.input}", groupId = "${spring.kafka.consumer.group-id}")
    public void onDataSourceOne(@Payload String message, @Headers MessageHeaders headers) {
        System.out.println("Data Recieved: "+message);
        log.info("========> Data from data source one: {}", message);
        try {
            service.findDataChange(Double.parseDouble(message));;
        } catch (Exception ex) {
            log.info("========> Failed to convert message:", ex);
        }
    }
}
