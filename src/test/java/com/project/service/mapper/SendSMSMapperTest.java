package com.project.service.mapper;

import static com.project.domain.SendSMSAsserts.*;
import static com.project.domain.SendSMSTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SendSMSMapperTest {

    private SendSMSMapper sendSMSMapper;

    @BeforeEach
    void setUp() {
        sendSMSMapper = new SendSMSMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSendSMSSample1();
        var actual = sendSMSMapper.toEntity(sendSMSMapper.toDto(expected));
        assertSendSMSAllPropertiesEquals(expected, actual);
    }
}
