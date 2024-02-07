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
    private final Duration windowSize = Duration.ofSeconds(dynamicWindowSize); //create a window size duration


//    @Autowired
//    public void processScore(StreamsBuilder streamsBuilder){
//
//
//        // Create a windowed stream of CDS_x and CDS_y topics
//
//        KTable<Windowed<String>, String> windowedStreamX = streamsBuilder.stream(inputX, Consumed.with(Serdes.String(), Serdes.String()))
//                .groupByKey()
//                .windowedBy(TimeWindows.of(windowSize))
//                .reduce((v1, v2) -> {
//                    int val =  Integer.parseInt(v1) + Integer.parseInt(v2);
//                    return val + "";
//                }).toStream()
//                //.peek((k,v)-> System.out.println("kx: "+k+" vx:"+ v))
//                .toTable();
//
//        KTable<Windowed<String>, String> windowedStreamY = streamsBuilder.stream(inputY, Consumed.with(Serdes.String(), Serdes.String()))
//                .groupByKey()
//                .windowedBy(TimeWindows.of(windowSize))
//                .reduce((v1, v2) -> {
//                    int val =  Integer.parseInt(v1) + Integer.parseInt(v2);
//                    return val + "";
//                }).toStream()
//                //.peek((k,v)-> System.out.println("ky: "+k+" vy:"+ v))
//                .toTable();
//        windowedStreamX
//                .join(windowedStreamY, (v1, v2) -> new Integer[]{Integer.parseInt(v1), Integer.parseInt(v2)})
//                .toStream()
//                .map((k, v) -> {
//                    int interaction = 0;
//                    if (v[0] > 0 && v[1] > 0 || v[0] == 0 && v[1] == 0) {
//                        interaction = 1;
//                    } else {
//                        interaction = -1;
//                    }
//                    return new KeyValue<>(new Object(), "x");
//                });
//    }

    @KafkaListener(topics = {"${kafka.topics.cds.one}", "${kafka.topics.cds.two}"}, groupId = "${spring.kafka.consumer.group-id}")
    public void getDataFroInputX(@Payload String value, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        System.out.println(value + " " + topic);

        if (Objects.equals(topic, inputX))
            SSTracker.getIntstance().addToWindowTopic1(Integer.parseInt(value));
        else
            SSTracker.getIntstance().addToWindowTopic2(Integer.parseInt(value));

        String api_name = topic.split("_")[1];

        System.out.println(api_name);

        int XORValue = SSHelper.getXORValue(SSTracker.getIntstance().windowedTopic1, SSTracker.getIntstance().windowedTopic2);

        System.out.println(XORValue);

        publishScore(XORValue, api_name);
    }


    public void publishScore(Integer score, String api_name){
        scoreKafka.send(SS_PREFIX, score.toString());
    }
}
