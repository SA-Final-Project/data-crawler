package miu.edu.normalizationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class NormalizationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NormalizationServiceApplication.class, args);
	}

}
