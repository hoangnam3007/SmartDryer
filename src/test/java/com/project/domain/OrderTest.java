package com.project.domain;

import static com.project.domain.CustomerTestSamples.*;
import static com.project.domain.DistrictTestSamples.*;
import static com.project.domain.HistoryOrderTestSamples.*;
import static com.project.domain.OrderTestSamples.*;
import static com.project.domain.ProvinceTestSamples.*;
import static com.project.domain.SaleTestSamples.*;
import static com.project.domain.SourceOrderTestSamples.*;
import static com.project.domain.StaffTestSamples.*;
import static com.project.domain.WardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.project.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Order.class);
        Order order1 = getOrderSample1();
        Order order2 = new Order();
        assertThat(order1).isNotEqualTo(order2);

        order2.setId(order1.getId());
        assertThat(order1).isEqualTo(order2);

        order2 = getOrderSample2();
        assertThat(order1).isNotEqualTo(order2);
    }

    @Test
    void historyOrderTest() {
        Order order = getOrderRandomSampleGenerator();
        HistoryOrder historyOrderBack = getHistoryOrderRandomSampleGenerator();

        order.addHistoryOrder(historyOrderBack);
        assertThat(order.getHistoryOrders()).containsOnly(historyOrderBack);
        assertThat(historyOrderBack.getOrder()).isEqualTo(order);

        order.removeHistoryOrder(historyOrderBack);
        assertThat(order.getHistoryOrders()).doesNotContain(historyOrderBack);
        assertThat(historyOrderBack.getOrder()).isNull();

        order.historyOrders(new HashSet<>(Set.of(historyOrderBack)));
        assertThat(order.getHistoryOrders()).containsOnly(historyOrderBack);
        assertThat(historyOrderBack.getOrder()).isEqualTo(order);

        order.setHistoryOrders(new HashSet<>());
        assertThat(order.getHistoryOrders()).doesNotContain(historyOrderBack);
        assertThat(historyOrderBack.getOrder()).isNull();
    }

    @Test
    void customerTest() {
        Order order = getOrderRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        order.setCustomer(customerBack);
        assertThat(order.getCustomer()).isEqualTo(customerBack);

        order.customer(null);
        assertThat(order.getCustomer()).isNull();
    }

    @Test
    void saleTest() {
        Order order = getOrderRandomSampleGenerator();
        Sale saleBack = getSaleRandomSampleGenerator();

        order.setSale(saleBack);
        assertThat(order.getSale()).isEqualTo(saleBack);

        order.sale(null);
        assertThat(order.getSale()).isNull();
    }

    @Test
    void staffTest() {
        Order order = getOrderRandomSampleGenerator();
        Staff staffBack = getStaffRandomSampleGenerator();

        order.setStaff(staffBack);
        assertThat(order.getStaff()).isEqualTo(staffBack);

        order.staff(null);
        assertThat(order.getStaff()).isNull();
    }

    @Test
    void sourceOrderTest() {
        Order order = getOrderRandomSampleGenerator();
        SourceOrder sourceOrderBack = getSourceOrderRandomSampleGenerator();

        order.setSourceOrder(sourceOrderBack);
        assertThat(order.getSourceOrder()).isEqualTo(sourceOrderBack);

        order.sourceOrder(null);
        assertThat(order.getSourceOrder()).isNull();
    }

    @Test
    void provinceTest() {
        Order order = getOrderRandomSampleGenerator();
        Province provinceBack = getProvinceRandomSampleGenerator();

        order.setProvince(provinceBack);
        assertThat(order.getProvince()).isEqualTo(provinceBack);

        order.province(null);
        assertThat(order.getProvince()).isNull();
    }

    @Test
    void districtTest() {
        Order order = getOrderRandomSampleGenerator();
        District districtBack = getDistrictRandomSampleGenerator();

        order.setDistrict(districtBack);
        assertThat(order.getDistrict()).isEqualTo(districtBack);

        order.district(null);
        assertThat(order.getDistrict()).isNull();
    }

    @Test
    void wardTest() {
        Order order = getOrderRandomSampleGenerator();
        Ward wardBack = getWardRandomSampleGenerator();

        order.setWard(wardBack);
        assertThat(order.getWard()).isEqualTo(wardBack);

        order.ward(null);
        assertThat(order.getWard()).isNull();
    }
}
