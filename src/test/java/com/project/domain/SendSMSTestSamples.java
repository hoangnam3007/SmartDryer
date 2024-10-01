package com.project.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SendSMSTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SendSMS getSendSMSSample1() {
        return new SendSMS().id(1L).mobile("mobile1").content("content1").status(1).type(1);
    }

    public static SendSMS getSendSMSSample2() {
        return new SendSMS().id(2L).mobile("mobile2").content("content2").status(2).type(2);
    }

    public static SendSMS getSendSMSRandomSampleGenerator() {
        return new SendSMS()
            .id(longCount.incrementAndGet())
            .mobile(UUID.randomUUID().toString())
            .content(UUID.randomUUID().toString())
            .status(intCount.incrementAndGet())
            .type(intCount.incrementAndGet());
    }
}
