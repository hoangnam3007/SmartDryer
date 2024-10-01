package com.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SendSMSDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SendSMSDTO.class);
        SendSMSDTO sendSMSDTO1 = new SendSMSDTO();
        sendSMSDTO1.setId(1L);
        SendSMSDTO sendSMSDTO2 = new SendSMSDTO();
        assertThat(sendSMSDTO1).isNotEqualTo(sendSMSDTO2);
        sendSMSDTO2.setId(sendSMSDTO1.getId());
        assertThat(sendSMSDTO1).isEqualTo(sendSMSDTO2);
        sendSMSDTO2.setId(2L);
        assertThat(sendSMSDTO1).isNotEqualTo(sendSMSDTO2);
        sendSMSDTO1.setId(null);
        assertThat(sendSMSDTO1).isNotEqualTo(sendSMSDTO2);
    }
}
