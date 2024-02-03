package com.rtds.RTDS.domain;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class RTDIS {

    @Value("${api_url}")
    private  String API_URL;
    
    @Value("${topic_name}")
    private  String KAFKA_TOPIC;

    @Autowired
    private KafkaTemplate<String,String> kafkaProducer;
    private ScheduledExecutorService scheduler;

    // Start streaming process
    public void startStreaming() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        // Schedule the stream method to run every 10 seconds
        scheduler.scheduleAtFixedRate(this::stream, 0, 10, TimeUnit.SECONDS);
    }

    public void stream() {
        // Create a WebClient instance
        WebClient webClient = WebClient.create();
        // Send an HTTP GET request to the API_URL
        webClient.get()
                .uri(API_URL)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(data -> {
                    // Print received message
                    System.out.println("Received msg: " + data);
                    // Send the received message to Kafka
                    kafkaProducer.send(KAFKA_TOPIC, data);
                    // Print confirmation message
                    System.out.println("Event sent to Kafka: " + data);
                });
    }



}
