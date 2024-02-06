package com.rtds.RTDS;

import com.rtds.RTDS.domain.RTDIS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RtdsApplication implements CommandLineRunner {
	@Autowired
	RTDIS rtdis;

	public static void main(String[] args) {
		SpringApplication.run(RtdsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//default url set if no command line argument is passed
		String api = "localhost:8081";

		for (String arg : args) {
			System.out.println("api: " + arg);
			api=arg;
		}

		rtdis.startStreaming(api);
	}
}
