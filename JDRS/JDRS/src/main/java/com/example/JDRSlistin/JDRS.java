package com.example.JDRSlistin;


import com.example.JDRSlistin.Sender.Sender;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;



@Service
public class JDRS {

     @Autowired
    Sender sender;
    @Value("${DS_px}")
    private  String KAFKA_TOPIC_qx;
    @Value("${DS_qty}")
    private  String KAFKA_TOPIC_qty;
    @KafkaListener(topics = "${topic_name}")
    public void receive(@Payload String message, @Headers MessageHeaders headers) {
        int startIndex = message.indexOf("{");
        message = message.substring(startIndex);

        JSONObject data = new JSONObject(message);
        JSONArray bids = data.getJSONArray("bids");
//
        if (bids.length() > 0) {
            JSONObject firstBid = bids.getJSONObject(0);
            double px = firstBid.getDouble("px");
            sender.send(KAFKA_TOPIC_qx, String.valueOf(px));
            double qty = firstBid.getDouble("qty");
            sender.send(KAFKA_TOPIC_qty, String.valueOf(qty));
            System.out.println("First bid: px = " + px + ", qty = " + qty);
        } else {
            System.out.println("No bids found.");
        }
        System.out.println("received message="+ message);


        // headers.keySet().forEach(key -> System.out.println(key+" : "+ headers.get(key)));
    }





}
