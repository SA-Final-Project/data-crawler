package edu.miu.kafka_ui;

import java.awt.Desktop;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import com.fasterxml.jackson.databind.ObjectMapper;

@EnableWebSocket
// @EnableKafka
@SpringBootApplication
public class KafkaUiApplication implements CommandLineRunner {
	static KafkaTemplate template;

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(KafkaUiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Runtime.getRuntime().exec(new String[] { "explorer", "http://localhost:8080" });

		DataSource ds = new DataSource();

		new Thread(ds.params).start();

		while (true) {
			System.out.println(ds.generateData());
			System.out.println(ds.params);
			Thread.sleep(1000);
		}
	}

	@Bean
	public ObjectMapper getMapper() {
		return new ObjectMapper();
	}

}
