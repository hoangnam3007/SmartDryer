package com.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SourceOrderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourceOrderDTO.class);
        SourceOrderDTO sourceOrderDTO1 = new SourceOrderDTO();
        sourceOrderDTO1.setId(1L);
        SourceOrderDTO sourceOrderDTO2 = new SourceOrderDTO();
        assertThat(sourceOrderDTO1).isNotEqualTo(sourceOrderDTO2);
        sourceOrderDTO2.setId(sourceOrderDTO1.getId());
        assertThat(sourceOrderDTO1).isEqualTo(sourceOrderDTO2);
        sourceOrderDTO2.setId(2L);
        assertThat(sourceOrderDTO1).isNotEqualTo(sourceOrderDTO2);
        sourceOrderDTO1.setId(null);
        assertThat(sourceOrderDTO1).isNotEqualTo(sourceOrderDTO2);
    }
}
