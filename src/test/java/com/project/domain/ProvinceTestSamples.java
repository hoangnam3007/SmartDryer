package com.project.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProvinceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Province getProvinceSample1() {
        return new Province().id(1L).code("code1").name("name1");
    }

    public static Province getProvinceSample2() {
        return new Province().id(2L).code("code2").name("name2");
    }

    public static Province getProvinceRandomSampleGenerator() {
        return new Province().id(longCount.incrementAndGet()).code(UUID.randomUUID().toString()).name(UUID.randomUUID().toString());
    }
}
