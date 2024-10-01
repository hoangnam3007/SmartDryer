package com.project.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SaleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Sale getSaleSample1() {
        return new Sale().id(1L).userName("userName1").fullName("fullName1").mobile("mobile1").email("email1").note("note1");
    }

    public static Sale getSaleSample2() {
        return new Sale().id(2L).userName("userName2").fullName("fullName2").mobile("mobile2").email("email2").note("note2");
    }

    public static Sale getSaleRandomSampleGenerator() {
        return new Sale()
            .id(longCount.incrementAndGet())
            .userName(UUID.randomUUID().toString())
            .fullName(UUID.randomUUID().toString())
            .mobile(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .note(UUID.randomUUID().toString());
    }
}
