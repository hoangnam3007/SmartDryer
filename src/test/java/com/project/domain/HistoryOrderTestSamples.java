package com.project.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HistoryOrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HistoryOrder getHistoryOrderSample1() {
        return new HistoryOrder().id(1L).modifiedBy("modifiedBy1");
    }

    public static HistoryOrder getHistoryOrderSample2() {
        return new HistoryOrder().id(2L).modifiedBy("modifiedBy2");
    }

    public static HistoryOrder getHistoryOrderRandomSampleGenerator() {
        return new HistoryOrder().id(longCount.incrementAndGet()).modifiedBy(UUID.randomUUID().toString());
    }
}
