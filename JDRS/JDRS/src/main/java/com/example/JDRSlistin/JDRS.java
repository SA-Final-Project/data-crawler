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
    public void processJsonMessage(@Payload String json, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws Exception {

        List<String[]> numberValues = new ArrayList<>();
        numberValues= JDRS.extractNumericValues(json);
        System.out.println("Got JSON from topic " + topic + " " + json);


        numberValues
                .forEach(values -> {
                    try {
                        publishDataElement(topic.split("_")[1] + "-" +
                                values[0], values[1]);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

//        System.out.println(numberValues);

    }
        public static List<String[]> extractNumericValues(String jsonString) {
            List<String[]> numericValues = new ArrayList<>();

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(jsonString);

                extractNumericValuesFromNode(rootNode, "", numericValues);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return numericValues;
        }

        private static void extractNumericValuesFromNode(JsonNode node, String currentPath,
                List<String[]> numericValues) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String fieldName = entry.getKey();
                JsonNode fieldValue = entry.getValue();
                String fullPath = currentPath.isEmpty() ? fieldName : currentPath + "." + fieldName;

                if (fieldValue.isNumber()) {
                    numericValues.add(new String[] { fullPath, fieldValue.toString() });
                } else if (fieldValue.isObject()) {
                    extractNumericValuesFromNode(fieldValue, fullPath, numericValues);

                } else if (fieldValue.isArray()) {
                    for (JsonNode field : fieldValue) {
                        extractNumericValuesFromNode(field, fullPath, numericValues);
                    }
                }
            }
        }


    private void publishDataElement(String name, String value) throws Exception {
        String topic = OUTPUT_TOPIC_PREFIX + name;

        adminClient.listTopics().names().thenApply(
                topics -> topics.stream().filter(topic::equals).findFirst()
                        .orElseGet(() -> {
                            System.out.println("New Topic found");
                            System.out.println("======================================================================");
                            kafkaTemplate.send("newRTDI", topic);
                            return null;
                        }));

//        System.out.println((adminClient.listTopics().names().get()));

        System.out.println("Sending data " + value + " on topic " + topic);

        kafkaTemplate.send(topic, value);
    }
}
