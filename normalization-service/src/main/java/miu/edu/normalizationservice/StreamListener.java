package miu.edu.normalizationservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class StreamListener {
    Map<String, List<Integer>> buffer = new HashMap<>();

    static final String TOPIC_PREFIX = "SS_";

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topicPattern = "SI_.*", groupId = "gid")
    public void listener(@Payload String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) throws Exception {

        List<Integer> topicBuffer = buffer.computeIfAbsent(topic, t -> new ArrayList<>());

        topicBuffer.add(Integer.parseInt(message));

        kafkaTemplate.send(TOPIC_PREFIX + topic.substring(3),
                String.valueOf(computeNormalizedValue(topicBuffer.size(), topic)));
    }

    public float computeNormalizedValue(int index, String msg) {
        Collection<List<Integer>> buffs = buffer.values();
        List<Integer> valuesToBeNormalized = new ArrayList<>();

        buffs.forEach(buff -> {
            try {
                int v = buff.get(index);
                valuesToBeNormalized.add(v);
            } catch (IndexOutOfBoundsException ex) {
            }
        });

        int max = valuesToBeNormalized.stream().mapToInt(i -> i).max().orElse(0);
        int min = valuesToBeNormalized.stream().mapToInt(i -> i).min().orElse(0);

        return (float) (Integer.parseInt(msg) - min) / (max - min);
    }

}
