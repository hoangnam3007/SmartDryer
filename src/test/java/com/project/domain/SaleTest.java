package com.project.domain;

import static com.project.domain.OrderTestSamples.*;
import static com.project.domain.SaleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.project.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SaleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sale.class);
        Sale sale1 = getSaleSample1();
        Sale sale2 = new Sale();
        assertThat(sale1).isNotEqualTo(sale2);

        sale2.setId(sale1.getId());
        assertThat(sale1).isEqualTo(sale2);

        sale2 = getSaleSample2();
        assertThat(sale1).isNotEqualTo(sale2);
    }

    @Test
    void orderTest() {
        Sale sale = getSaleRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        sale.addOrder(orderBack);
        assertThat(sale.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getSale()).isEqualTo(sale);

        sale.removeOrder(orderBack);
        assertThat(sale.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getSale()).isNull();

        sale.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(sale.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getSale()).isEqualTo(sale);

        sale.setOrders(new HashSet<>());
        assertThat(sale.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getSale()).isNull();
    }
}
