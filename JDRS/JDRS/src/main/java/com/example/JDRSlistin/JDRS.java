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
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JDRS {

    @Value("${spring.kafka.bootstrap-servers}")
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
    public void processJsonMessage(@Payload String json, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        List<String[]> numberValues = new ArrayList<>();

        System.out.println("Got JSON from topic " + topic + " " + json);

        Pattern pattern = Pattern.compile("\"(\\w+)\":\\s*([0-9]+(?:\\.[0-9]+)?)");
        Matcher patternMatcher = pattern.matcher(json);
        while (patternMatcher.find()) {
            numberValues.add(new String[] { patternMatcher.group(1), patternMatcher.group(2) });
        }

        numberValues
                .forEach(values -> publishDataElement(topic.split("_")[1] + "_" +
                        values[0], values[1]));

        System.out.println(numberValues);
    }

    private void publishDataElement(String name, String value) {
        String topic = OUTPUT_TOPIC_PREFIX + name;

        adminClient.listTopics().names().thenApply(
                topics -> topics.stream().filter(topic::equals).findFirst()
                        .orElseGet(() -> {
                            System.out.println("New Topic found");
                            kafkaTemplate.send("newRTDI", topic);
                            return null;
                        }));

        System.out.println("Sending data " + value + " on topic " + topic);

        kafkaTemplate.send(topic, value);
    }
}
