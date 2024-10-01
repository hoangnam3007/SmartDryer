package com.project.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Order getOrderSample1() {
        return new Order()
            .id(1L)
            .code("code1")
            .createBy("createBy1")
            .amount(1L)
            .saleNote("saleNote1")
            .techNote("techNote1")
            .note("note1")
            .materialSource(1)
            .cusName("cusName1")
            .cusAddress("cusAddress1")
            .cusMobile("cusMobile1")
            .imageURL("imageURL1")
            .activation(1)
            .assignBy("assignBy1");
    }

    public static Order getOrderSample2() {
        return new Order()
            .id(2L)
            .code("code2")
            .createBy("createBy2")
            .amount(2L)
            .saleNote("saleNote2")
            .techNote("techNote2")
            .note("note2")
            .materialSource(2)
            .cusName("cusName2")
            .cusAddress("cusAddress2")
            .cusMobile("cusMobile2")
            .imageURL("imageURL2")
            .activation(2)
            .assignBy("assignBy2");
    }

    public static Order getOrderRandomSampleGenerator() {
        return new Order()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .createBy(UUID.randomUUID().toString())
            .amount(longCount.incrementAndGet())
            .saleNote(UUID.randomUUID().toString())
            .techNote(UUID.randomUUID().toString())
            .note(UUID.randomUUID().toString())
            .materialSource(intCount.incrementAndGet())
            .cusName(UUID.randomUUID().toString())
            .cusAddress(UUID.randomUUID().toString())
            .cusMobile(UUID.randomUUID().toString())
            .imageURL(UUID.randomUUID().toString())
            .activation(intCount.incrementAndGet())
            .assignBy(UUID.randomUUID().toString());
    }
}
