package com.example.JDRSlistin;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Service
public class JDRS {
    private static final String OUTPUT_TOPIC_PREFIX = "RTDI_";

    private static final Logger logger = LoggerFactory.getLogger(JDRS.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "RTD")
    public void processJsonMessage(ConsumerRecord<String, String> record) {
        String json = record.value();

        try {
            extractNumericValues(json);
        } catch (Exception e) {
            logger.error("Error processing JSON: " + e.getMessage());
        }
    }



    public Map<String, Number> extractNumericValues(String jsonString) {
        Map<String, Number> numericValues = new HashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonString);

            extractNumericValuesFromNode(rootNode, "", numericValues);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return numericValues;
    }

    private void extractNumericValuesFromNode(JsonNode node, String currentPath, Map<String, Number> numericValues) {
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String fieldName = entry.getKey();
            JsonNode fieldValue = entry.getValue();
            String fullPath = currentPath.isEmpty() ? fieldName : currentPath + "." + fieldName;

            if (fieldValue.isNumber()) {
                numericValues.put(fullPath, fieldValue.numberValue());
                publishDataElement(fullPath, fieldValue.numberValue().toString());
            } else if (fieldValue.isObject()) {
                extractNumericValuesFromNode(fieldValue, fullPath, numericValues);

            }
            else if (fieldValue.isArray()) {
                for (JsonNode field : fieldValue) {
                    extractNumericValuesFromNode(field, fullPath, numericValues);
                }
            }
        }
    }
    private void publishDataElement(String name, String value) {
        String topic = OUTPUT_TOPIC_PREFIX + name;
        kafkaTemplate.send(topic, name, value);
    }
}
