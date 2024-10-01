package com.project.domain;

import static com.project.domain.OrderTestSamples.*;
import static com.project.domain.SourceOrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.project.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SourceOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourceOrder.class);
        SourceOrder sourceOrder1 = getSourceOrderSample1();
        SourceOrder sourceOrder2 = new SourceOrder();
        assertThat(sourceOrder1).isNotEqualTo(sourceOrder2);

        sourceOrder2.setId(sourceOrder1.getId());
        assertThat(sourceOrder1).isEqualTo(sourceOrder2);

        sourceOrder2 = getSourceOrderSample2();
        assertThat(sourceOrder1).isNotEqualTo(sourceOrder2);
    }

    @Test
    void orderTest() {
        SourceOrder sourceOrder = getSourceOrderRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        sourceOrder.addOrder(orderBack);
        assertThat(sourceOrder.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getSourceOrder()).isEqualTo(sourceOrder);

        sourceOrder.removeOrder(orderBack);
        assertThat(sourceOrder.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getSourceOrder()).isNull();

        sourceOrder.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(sourceOrder.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getSourceOrder()).isEqualTo(sourceOrder);

        sourceOrder.setOrders(new HashSet<>());
        assertThat(sourceOrder.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getSourceOrder()).isNull();
    }
}
