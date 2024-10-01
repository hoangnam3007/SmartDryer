package com.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerEquipmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerEquipmentDTO.class);
        CustomerEquipmentDTO customerEquipmentDTO1 = new CustomerEquipmentDTO();
        customerEquipmentDTO1.setId(1L);
        CustomerEquipmentDTO customerEquipmentDTO2 = new CustomerEquipmentDTO();
        assertThat(customerEquipmentDTO1).isNotEqualTo(customerEquipmentDTO2);
        customerEquipmentDTO2.setId(customerEquipmentDTO1.getId());
        assertThat(customerEquipmentDTO1).isEqualTo(customerEquipmentDTO2);
        customerEquipmentDTO2.setId(2L);
        assertThat(customerEquipmentDTO1).isNotEqualTo(customerEquipmentDTO2);
        customerEquipmentDTO1.setId(null);
        assertThat(customerEquipmentDTO1).isNotEqualTo(customerEquipmentDTO2);
    }
}
