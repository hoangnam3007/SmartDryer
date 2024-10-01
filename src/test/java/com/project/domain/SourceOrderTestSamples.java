package com.project.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SourceOrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SourceOrder getSourceOrderSample1() {
        return new SourceOrder().id(1L).name("name1").description("description1");
    }

    public static SourceOrder getSourceOrderSample2() {
        return new SourceOrder().id(2L).name("name2").description("description2");
    }

    public static SourceOrder getSourceOrderRandomSampleGenerator() {
        return new SourceOrder()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
