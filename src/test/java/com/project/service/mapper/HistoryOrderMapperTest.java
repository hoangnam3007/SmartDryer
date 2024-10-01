package com.project.service.mapper;

import static com.project.domain.HistoryOrderAsserts.*;
import static com.project.domain.HistoryOrderTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoryOrderMapperTest {

    private HistoryOrderMapper historyOrderMapper;

    @BeforeEach
    void setUp() {
        historyOrderMapper = new HistoryOrderMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHistoryOrderSample1();
        var actual = historyOrderMapper.toEntity(historyOrderMapper.toDto(expected));
        assertHistoryOrderAllPropertiesEquals(expected, actual);
    }
}
