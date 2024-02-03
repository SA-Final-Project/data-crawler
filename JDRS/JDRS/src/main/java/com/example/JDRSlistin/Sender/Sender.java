package com.example.JDRSlistin.Sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.kafka.core.KafkaTemplate;
        import org.springframework.stereotype.Service;

@Service
public class Sender  {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;



    public void send(String topic ,String person){
       // System.out.println("sending person="+person +" to topic="+ topic);
        if (topic == "qty") {

            kafkaTemplate.send(topic, person);
            System.out.println("qty" + topic);
        }else {
            kafkaTemplate.send(topic, person);
            System.out.println("px" + topic);
        }

    }



}
