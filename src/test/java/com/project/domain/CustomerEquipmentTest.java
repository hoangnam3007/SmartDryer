package com.project.domain;

import static com.project.domain.CustomerEquipmentTestSamples.*;
import static com.project.domain.CustomerTestSamples.*;
import static com.project.domain.EquipmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerEquipmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerEquipment.class);
        CustomerEquipment customerEquipment1 = getCustomerEquipmentSample1();
        CustomerEquipment customerEquipment2 = new CustomerEquipment();
        assertThat(customerEquipment1).isNotEqualTo(customerEquipment2);

        customerEquipment2.setId(customerEquipment1.getId());
        assertThat(customerEquipment1).isEqualTo(customerEquipment2);

        customerEquipment2 = getCustomerEquipmentSample2();
        assertThat(customerEquipment1).isNotEqualTo(customerEquipment2);
    }

    @Test
    void customerTest() {
        CustomerEquipment customerEquipment = getCustomerEquipmentRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        customerEquipment.setCustomer(customerBack);
        assertThat(customerEquipment.getCustomer()).isEqualTo(customerBack);

        customerEquipment.customer(null);
        assertThat(customerEquipment.getCustomer()).isNull();
    }

    @Test
    void equipmentTest() {
        CustomerEquipment customerEquipment = getCustomerEquipmentRandomSampleGenerator();
        Equipment equipmentBack = getEquipmentRandomSampleGenerator();

        customerEquipment.setEquipment(equipmentBack);
        assertThat(customerEquipment.getEquipment()).isEqualTo(equipmentBack);

        customerEquipment.equipment(null);
        assertThat(customerEquipment.getEquipment()).isNull();
    }
}
