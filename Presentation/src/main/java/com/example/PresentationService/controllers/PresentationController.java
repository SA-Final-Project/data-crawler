package com.example.PresentationService.controllers;

import com.example.PresentationService.domains.DataPoint;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@EnableScheduling
public class PresentationController {

    private final Map<String, List<DataPoint>> topicDataMap;



    public PresentationController() {
               this.topicDataMap = new ConcurrentHashMap<>();
        //subscribeToAllTopics();
    }

    // Rest controller to get the data per topic
    @GetMapping("/topics/{topicName}")
    public List<DataPoint> getTopicData(@PathVariable String topicName) {
        return topicDataMap.getOrDefault(topicName, new ArrayList<>());
    }

    // A message listener for all the topics
    @KafkaListener(topics = {"${kafka.topic1}", "${kafka.topic2}"})
    public void receiveData(@Payload String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        System.out.println("Received data: " + message + " for topic: " + topic);
        List<DataPoint> dataPoints = topicDataMap.getOrDefault(topic, new ArrayList<>());
        DataPoint newDataPoint = parseData(message); // Implement your data parsing logic
        dataPoints.add(newDataPoint);
        topicDataMap.put(topic, dataPoints);
    }


    // A scheduled method to generate a CSV file for each topic
    @Scheduled(fixedDelay = 60000) // Runs every minute to update CSV files
    public void generateCSVFiles() {
        System.out.println("Generating the csv file..");
        for (String topicName : topicDataMap.keySet()) {
            List<DataPoint> dataPoints = topicDataMap.get(topicName);
            generateCSVFile(topicName, dataPoints);
        }
    }



    // Helper method to parse the data and create a DataPoint object
    private DataPoint parseData(String message) {
        Instant timestamp = Instant.now();
        double value = Double.parseDouble(message);
        return new DataPoint(timestamp, value);
    }

    // Helper method to generate the CSV file
    private void generateCSVFile(String topicName, List<DataPoint> dataPoints) {
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(topicName + ".csv"), CSVFormat.DEFAULT)) {
            for (DataPoint dataPoint : dataPoints) {
                csvPrinter.printRecord(dataPoint.getTimestamp(), dataPoint.getValue());
            }
        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
        }
    }
}