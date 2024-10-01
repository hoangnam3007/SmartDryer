package com.project.service.mapper;

import static com.project.domain.SourceOrderAsserts.*;
import static com.project.domain.SourceOrderTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SourceOrderMapperTest {

    private SourceOrderMapper sourceOrderMapper;

    @BeforeEach
    void setUp() {
        sourceOrderMapper = new SourceOrderMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSourceOrderSample1();
        var actual = sourceOrderMapper.toEntity(sourceOrderMapper.toDto(expected));
        assertSourceOrderAllPropertiesEquals(expected, actual);
    }
}
