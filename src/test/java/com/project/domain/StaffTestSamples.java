package com.project.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StaffTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Staff getStaffSample1() {
        return new Staff()
            .id(1L)
            .userName("userName1")
            .fullName("fullName1")
            .mobile("mobile1")
            .email("email1")
            .note("note1")
            .isLead(1)
            .imageURL("imageURL1");
    }

    public static Staff getStaffSample2() {
        return new Staff()
            .id(2L)
            .userName("userName2")
            .fullName("fullName2")
            .mobile("mobile2")
            .email("email2")
            .note("note2")
            .isLead(2)
            .imageURL("imageURL2");
    }

    public static Staff getStaffRandomSampleGenerator() {
        return new Staff()
            .id(longCount.incrementAndGet())
            .userName(UUID.randomUUID().toString())
            .fullName(UUID.randomUUID().toString())
            .mobile(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .note(UUID.randomUUID().toString())
            .isLead(intCount.incrementAndGet())
            .imageURL(UUID.randomUUID().toString());
    }
}
