package edu.miu.kafka_ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@EnableKafka
@SpringBootApplication
public class KafkaUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaUiApplication.class, args);
	}

}
