package com.project.domain;

import static com.project.domain.CusNoteTestSamples.*;
import static com.project.domain.CustomerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CusNoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CusNote.class);
        CusNote cusNote1 = getCusNoteSample1();
        CusNote cusNote2 = new CusNote();
        assertThat(cusNote1).isNotEqualTo(cusNote2);

        cusNote2.setId(cusNote1.getId());
        assertThat(cusNote1).isEqualTo(cusNote2);

        cusNote2 = getCusNoteSample2();
        assertThat(cusNote1).isNotEqualTo(cusNote2);
    }

    @Test
    void customerTest() {
        CusNote cusNote = getCusNoteRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        cusNote.setCustomer(customerBack);
        assertThat(cusNote.getCustomer()).isEqualTo(customerBack);

        cusNote.customer(null);
        assertThat(cusNote.getCustomer()).isNull();
    }
}
