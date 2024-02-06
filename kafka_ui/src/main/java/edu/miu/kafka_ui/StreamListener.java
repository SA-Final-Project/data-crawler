package edu.miu.kafka_ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class StreamListener {

    @Autowired
    SocketTextHandler socketTextHandler;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @KafkaListener(topicPattern = ".*", groupId = "gid")
    public void listener(@Payload String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("Received Message: " + message);

        socketTextHandler.sendMessage(objectMapper.writeValueAsString(new Message(message, topic, ts)));
    }

    public void sendMsg(String topic, String msg) {
        kafkaTemplate.send(topic, msg);
    }

}
