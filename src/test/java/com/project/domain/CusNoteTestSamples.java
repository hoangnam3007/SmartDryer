package com.project.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CusNoteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CusNote getCusNoteSample1() {
        return new CusNote().id(1L).createBy("createBy1").content("content1");
    }

    public static CusNote getCusNoteSample2() {
        return new CusNote().id(2L).createBy("createBy2").content("content2");
    }

    public static CusNote getCusNoteRandomSampleGenerator() {
        return new CusNote().id(longCount.incrementAndGet()).createBy(UUID.randomUUID().toString()).content(UUID.randomUUID().toString());
    }
}
