package edu.miu.kafka_ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class DataSource {

    Map<String, Object> jsonSample = new HashMap<>();

    ChangeParameters params = ChangeParameters.instance;

    DataSource() {
        jsonSample.put("generatedBy", "Data Generator v1");
        jsonSample.put("author", "Abel");
        jsonSample.put("madeIn", "America");
        jsonSample.put("date", "2/7/2024");

        System.out.println(params);
    }

    Map<String, Object> generateData() {
        Map<String, Integer> data = new HashMap<>();

        for (int i = 0; i < params.getNumberOfDataPoints(); i++) {
            Random rand = new Random((long) (params.ticker * params.getStartingPoints().get(i)));

            int spike = 1;

            if (params.getSpikeRate() % params.ticker == 0) {
                spike = params.getSpikeRate();
            }

            data.put("dataPoint" + i, rand.nextInt(params.getDataLowerBound(), params.getDataUpperBound()) * spike);
        }

        jsonSample.put("data", data);
        return jsonSample;
    }

}
