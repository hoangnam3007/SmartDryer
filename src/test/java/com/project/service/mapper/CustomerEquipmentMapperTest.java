package com.project.service.mapper;

import static com.project.domain.CustomerEquipmentAsserts.*;
import static com.project.domain.CustomerEquipmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerEquipmentMapperTest {

    private CustomerEquipmentMapper customerEquipmentMapper;

    @BeforeEach
    void setUp() {
        customerEquipmentMapper = new CustomerEquipmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCustomerEquipmentSample1();
        var actual = customerEquipmentMapper.toEntity(customerEquipmentMapper.toDto(expected));
        assertCustomerEquipmentAllPropertiesEquals(expected, actual);
    }
}
