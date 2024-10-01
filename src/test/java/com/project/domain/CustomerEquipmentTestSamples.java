package com.project.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerEquipmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CustomerEquipment getCustomerEquipmentSample1() {
        return new CustomerEquipment().id(1L).quantily(1);
    }

    public static CustomerEquipment getCustomerEquipmentSample2() {
        return new CustomerEquipment().id(2L).quantily(2);
    }

    public static CustomerEquipment getCustomerEquipmentRandomSampleGenerator() {
        return new CustomerEquipment().id(longCount.incrementAndGet()).quantily(intCount.incrementAndGet());
    }
}
