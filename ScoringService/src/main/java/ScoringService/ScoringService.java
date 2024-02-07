package ScoringService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

@Component
public class ScoringService {
    @Autowired
    KafkaTemplate<String, String> scoreKafka;
    @Value("${kafka.topics.cds.one}")
    private String inputX;
    @Value("${kafka.topics.cds.two}")
    private String inputY;

    private static final String SS_PREFIX = "SS_";
    private int dynamicWindowSize = 5;
    private final Duration windowSize = Duration.ofSeconds(dynamicWindowSize); // create a window size duration

    @KafkaListener(topics = { "${kafka.topics.cds.one}",
            "${kafka.topics.cds.two}" }, groupId = "${spring.kafka.consumer.group-id}")
    public void getDataFroInputX(@Payload String value, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        System.out.println(value + " " + topic);

        if (Objects.equals(topic, inputX))
            SSTracker.getIntstance().addToWindowTopic1(Integer.parseInt(value));
        else
            SSTracker.getIntstance().addToWindowTopic2(Integer.parseInt(value));

        String api_name = topic.split("_")[1];

        System.out.println("Got data " + value + " from topic " + topic + " from API " + api_name);

        int XORValue = SSHelper.getXORValue(SSTracker.getIntstance().windowedTopic1,
                SSTracker.getIntstance().windowedTopic2);

        System.out.println(XORValue);

        System.out.println(
                "Publishing value " + XORValue + " to topic " + SS_PREFIX + inputX.split("_")[1] + "_"
                        + inputY.split("_")[1]);

        publishScore(XORValue, api_name);
    }

    public void publishScore(Integer score, String api_name) {
        scoreKafka.send(SS_PREFIX + inputX.split("_")[1] + "_" + inputY.split("_")[1], score.toString());
    }
}
