package edu.miu.kafka_ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

import org.apache.kafka.common.internals.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SocketTextHandler extends TextWebSocketHandler {
    List<WebSocketSession> sessions = Collections.synchronizedList(new ArrayList<>());

    KafkaTemplate<String, String> kafkaTemplate;

    SocketTextHandler(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        try {
            for (WebSocketSession session : sessions) {
                session.sendMessage(new TextMessage(message));
            }
        } catch (IOException ex) {
            System.out.println("Error happened");
            System.out.println(ex);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Message msg = objectMapper.readValue(message.getPayload(), Message.class);
        kafkaTemplate.send(msg.topic, msg.msg);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        System.out.println("Connected");
        sessions.add(session);
        System.out.println(sessions);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessions.remove(session);
        System.out.println("Connection Closed");
    }
}
