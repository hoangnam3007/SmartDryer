package com.project.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WardTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Ward getWardSample1() {
        return new Ward().id(1L).code("code1").name("name1");
    }

    public static Ward getWardSample2() {
        return new Ward().id(2L).code("code2").name("name2");
    }

    public static Ward getWardRandomSampleGenerator() {
        return new Ward().id(longCount.incrementAndGet()).code(UUID.randomUUID().toString()).name(UUID.randomUUID().toString());
    }
}
