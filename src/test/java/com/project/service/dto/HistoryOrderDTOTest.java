package com.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoryOrderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoryOrderDTO.class);
        HistoryOrderDTO historyOrderDTO1 = new HistoryOrderDTO();
        historyOrderDTO1.setId(1L);
        HistoryOrderDTO historyOrderDTO2 = new HistoryOrderDTO();
        assertThat(historyOrderDTO1).isNotEqualTo(historyOrderDTO2);
        historyOrderDTO2.setId(historyOrderDTO1.getId());
        assertThat(historyOrderDTO1).isEqualTo(historyOrderDTO2);
        historyOrderDTO2.setId(2L);
        assertThat(historyOrderDTO1).isNotEqualTo(historyOrderDTO2);
        historyOrderDTO1.setId(null);
        assertThat(historyOrderDTO1).isNotEqualTo(historyOrderDTO2);
    }
}
