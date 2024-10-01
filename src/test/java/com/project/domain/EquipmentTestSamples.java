package com.project.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EquipmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Equipment getEquipmentSample1() {
        return new Equipment().id(1L).equipmentCode("equipmentCode1").description("description1");
    }

    public static Equipment getEquipmentSample2() {
        return new Equipment().id(2L).equipmentCode("equipmentCode2").description("description2");
    }

    public static Equipment getEquipmentRandomSampleGenerator() {
        return new Equipment()
            .id(longCount.incrementAndGet())
            .equipmentCode(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
