package edu.miu.kafka_ui;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

        @Bean
        public ProducerFactory<String, String> producerFactory() {
                Map<String, Object> configProps = new HashMap<>();
                configProps.put(
                                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                                "157.230.200.212:9092");
                configProps.put(
                                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                                StringSerializer.class);
                configProps.put(
                                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                                StringSerializer.class);
                configProps.put(
                                ProducerConfig.METADATA_MAX_AGE_CONFIG,
                                500);
                return new DefaultKafkaProducerFactory<>(configProps);
        }

        @Bean
        public KafkaTemplate<String, String> kafkaTemplate() {
                return new KafkaTemplate<>(producerFactory());
        }
}
