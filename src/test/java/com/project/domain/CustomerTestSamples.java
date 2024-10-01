package com.project.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Customer getCustomerSample1() {
        return new Customer()
            .id(1L)
            .userName("userName1")
            .code("code1")
            .displayName("displayName1")
            .address("address1")
            .createBy("createBy1")
            .mobile("mobile1")
            .email("email1")
            .source("source1")
            .note("note1");
    }

    public static Customer getCustomerSample2() {
        return new Customer()
            .id(2L)
            .userName("userName2")
            .code("code2")
            .displayName("displayName2")
            .address("address2")
            .createBy("createBy2")
            .mobile("mobile2")
            .email("email2")
            .source("source2")
            .note("note2");
    }

    public static Customer getCustomerRandomSampleGenerator() {
        return new Customer()
            .id(longCount.incrementAndGet())
            .userName(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .displayName(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .createBy(UUID.randomUUID().toString())
            .mobile(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .source(UUID.randomUUID().toString())
            .note(UUID.randomUUID().toString());
    }
}
