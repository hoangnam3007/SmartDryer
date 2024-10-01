package com.project.service.mapper;

import static com.project.domain.CusNoteAsserts.*;
import static com.project.domain.CusNoteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CusNoteMapperTest {

    private CusNoteMapper cusNoteMapper;

    @BeforeEach
    void setUp() {
        cusNoteMapper = new CusNoteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCusNoteSample1();
        var actual = cusNoteMapper.toEntity(cusNoteMapper.toDto(expected));
        assertCusNoteAllPropertiesEquals(expected, actual);
    }
}
