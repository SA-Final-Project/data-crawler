package edu.miu.kafka_ui;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class Message {
    String topic;
    String msg;
    long ts;
}