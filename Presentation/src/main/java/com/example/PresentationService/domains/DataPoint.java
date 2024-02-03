package com.example.PresentationService.domains;

import java.time.Instant;

public class DataPoint {
    private final Instant timestamp;
    private final double value;

    public DataPoint(Instant timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public double getValue() {
        return value;
    }
}

