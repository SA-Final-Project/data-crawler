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
    public void processJsonMessage(ConsumerRecord<String, String> record) throws Exception{
        String json = record.value();
       // json = "{\"project_name\":\"Web Data Integration System\",\"legacy_systems\":[{\"name\":\"EvenTracker\",\"microservices_count\":8,\"data_analysis_time\":500},{\"name\":\"WebDataHooks\",\"number_of_hooks\":5,\"api_call_limit\":1000}],\"integration\":{\"docker_containers\":true,\"github_commits_per_day\":20,\"orchestration_tool\":\"Docker Compose\",\"ci_cd_tool\":\"Jenkins\"},\"services\":[{\"name\":\"Web Data Finder Service (WDFS)\",\"search_interval_minutes\":15,\"maximum_new_apis_per_search\":10},{\"name\":\"Availability Checker Service (ACS)\",\"api_check_interval_seconds\":30,\"maximum_api_checks_per_minute\":50},{\"name\":\"CSS\",\"rtdis_code_generation_time_seconds\":5,\"maximum_concurrent_requests\":20},{\"name\":\"DSGS\",\"new_api_check_interval_hours\":2,\"maximum_execution_time_minutes\":30}],\"frontend\":{\"dashboard\":true,\"refresh_rate_seconds\":10,\"maximum_displayed_activities\":50,\"filtering_capability\":true,\"maximum_disallowed_apis_displayed\":20}}";
//        System.out.println(json);
//        try {
            List<String[]> numberValues = JSONNumberValueExtractor.extractNumericValues(json);
        System.out.println(numberValues);
            numberValues
                    .forEach(value -> publishDataElement(record.topic().split("_")[1] + "_" + value[0], value[1]));
//        } catch (Exception e) {
//            logger.error("Error processing JSON: " + e.getMessage());
//        }

        System.out.println(numberValues);
    }

    private void publishDataElement(String name, String value) {
        String topic = OUTPUT_TOPIC_PREFIX + name;

        adminClient.listTopics().names().thenApply(
                topics -> topics.stream().filter(topic::equals).findFirst()
                        .orElseGet(() -> {
                            kafkaTemplate.send("newRTDI", topic);
                            return null;
                        }));
        System.out.println(topic);
        kafkaTemplate.send(topic, name, value);
    }
}
