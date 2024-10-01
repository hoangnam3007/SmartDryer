package com.project.domain;

import static com.project.domain.HistoryOrderTestSamples.*;
import static com.project.domain.OrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoryOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoryOrder.class);
        HistoryOrder historyOrder1 = getHistoryOrderSample1();
        HistoryOrder historyOrder2 = new HistoryOrder();
        assertThat(historyOrder1).isNotEqualTo(historyOrder2);

        historyOrder2.setId(historyOrder1.getId());
        assertThat(historyOrder1).isEqualTo(historyOrder2);

        historyOrder2 = getHistoryOrderSample2();
        assertThat(historyOrder1).isNotEqualTo(historyOrder2);
    }

    @Test
    void orderTest() {
        HistoryOrder historyOrder = getHistoryOrderRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        historyOrder.setOrder(orderBack);
        assertThat(historyOrder.getOrder()).isEqualTo(orderBack);

        historyOrder.order(null);
        assertThat(historyOrder.getOrder()).isNull();
    }
}
