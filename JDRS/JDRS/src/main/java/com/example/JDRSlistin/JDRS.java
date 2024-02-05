package com.example.JDRSlistin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Properties;

@Component
public class JDRS {

    @Value("${kafka.bootstrap-servers}")
    String bootstrapServers;

    AdminClient adminClient;

    @PostConstruct
    void postConst() {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        adminClient = AdminClient.create(properties);
    }

    private static final String OUTPUT_TOPIC_PREFIX = "RTDI_";

    private static final Logger logger = LoggerFactory.getLogger(JDRS.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topicPattern = "RTD_.*", groupId = "gid")
    public void processJsonMessage(ConsumerRecord<String, String> record) {
        String json = record.value();

        try {
            List<String[]> numberValues = JSONNumberValueExtractor.extractNumericValues(json);
            numberValues
                    .forEach(values -> publishDataElement(record.topic().split("_")[1] + "_" + values[0], values[1]));
        } catch (Exception e) {
            logger.error("Error processing JSON: " + e.getMessage());
        }
    }

    private void publishDataElement(String name, String value) {
        String topic = OUTPUT_TOPIC_PREFIX + name;

        adminClient.listTopics().names().thenApply(
                topics -> topics.stream().filter(topic::equals).findFirst()
                        .orElseGet(() -> {
                            kafkaTemplate.send("newRTDI", topic);
                            return null;
                        }));

        kafkaTemplate.send(topic, name, value);
    }
}
