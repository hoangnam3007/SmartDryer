package com.project.domain;

import static com.project.domain.SendSMSTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SendSMSTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SendSMS.class);
        SendSMS sendSMS1 = getSendSMSSample1();
        SendSMS sendSMS2 = new SendSMS();
        assertThat(sendSMS1).isNotEqualTo(sendSMS2);

        sendSMS2.setId(sendSMS1.getId());
        assertThat(sendSMS1).isEqualTo(sendSMS2);

        sendSMS2 = getSendSMSSample2();
        assertThat(sendSMS1).isNotEqualTo(sendSMS2);
    }
}
