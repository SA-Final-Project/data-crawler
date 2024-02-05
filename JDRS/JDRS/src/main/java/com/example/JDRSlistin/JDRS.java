package com.example.JDRSlistin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class JDRS {
    private static final String OUTPUT_TOPIC_PREFIX = "RTDI_";

    private static final Logger logger = LoggerFactory.getLogger(JDRS.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topicPattern = "RTD_.*")
    public void processJsonMessage(ConsumerRecord<String, String> record, @Header()) {
        String json = record.value();

        try {
            List<String[]> numberValues = JSONNumberValueExtractor.extractNumericValues(json);
            numberValues.forEach(values -> publishDataElement(values[0], values[1]));
        } catch (Exception e) {
            logger.error("Error processing JSON: " + e.getMessage());
        }
    }

    private void publishDataElement(String name, String value) {
        String topic = OUTPUT_TOPIC_PREFIX + name;
        kafkaTemplate.send(topic, name, value);
    }
}
