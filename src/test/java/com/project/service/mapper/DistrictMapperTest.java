package com.project.service.mapper;

import static com.project.domain.DistrictAsserts.*;
import static com.project.domain.DistrictTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DistrictMapperTest {

    private DistrictMapper districtMapper;

    @BeforeEach
    void setUp() {
        districtMapper = new DistrictMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDistrictSample1();
        var actual = districtMapper.toEntity(districtMapper.toDto(expected));
        assertDistrictAllPropertiesEquals(expected, actual);
    }
}
