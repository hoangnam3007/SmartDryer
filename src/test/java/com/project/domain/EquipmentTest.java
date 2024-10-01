package com.project.domain;

import static com.project.domain.CustomerEquipmentTestSamples.*;
import static com.project.domain.EquipmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.project.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EquipmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Equipment.class);
        Equipment equipment1 = getEquipmentSample1();
        Equipment equipment2 = new Equipment();
        assertThat(equipment1).isNotEqualTo(equipment2);

        equipment2.setId(equipment1.getId());
        assertThat(equipment1).isEqualTo(equipment2);

        equipment2 = getEquipmentSample2();
        assertThat(equipment1).isNotEqualTo(equipment2);
    }

    @Test
    void customerEquipmentTest() {
        Equipment equipment = getEquipmentRandomSampleGenerator();
        CustomerEquipment customerEquipmentBack = getCustomerEquipmentRandomSampleGenerator();

        equipment.addCustomerEquipment(customerEquipmentBack);
        assertThat(equipment.getCustomerEquipments()).containsOnly(customerEquipmentBack);
        assertThat(customerEquipmentBack.getEquipment()).isEqualTo(equipment);

        equipment.removeCustomerEquipment(customerEquipmentBack);
        assertThat(equipment.getCustomerEquipments()).doesNotContain(customerEquipmentBack);
        assertThat(customerEquipmentBack.getEquipment()).isNull();

        equipment.customerEquipments(new HashSet<>(Set.of(customerEquipmentBack)));
        assertThat(equipment.getCustomerEquipments()).containsOnly(customerEquipmentBack);
        assertThat(customerEquipmentBack.getEquipment()).isEqualTo(equipment);

        equipment.setCustomerEquipments(new HashSet<>());
        assertThat(equipment.getCustomerEquipments()).doesNotContain(customerEquipmentBack);
        assertThat(customerEquipmentBack.getEquipment()).isNull();
    }
}
