package com.project.domain;

import static com.project.domain.CustomerTestSamples.*;
import static com.project.domain.DistrictTestSamples.*;
import static com.project.domain.OrderTestSamples.*;
import static com.project.domain.WardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.project.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class WardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ward.class);
        Ward ward1 = getWardSample1();
        Ward ward2 = new Ward();
        assertThat(ward1).isNotEqualTo(ward2);

        ward2.setId(ward1.getId());
        assertThat(ward1).isEqualTo(ward2);

        ward2 = getWardSample2();
        assertThat(ward1).isNotEqualTo(ward2);
    }

    @Test
    void customerTest() {
        Ward ward = getWardRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        ward.addCustomer(customerBack);
        assertThat(ward.getCustomers()).containsOnly(customerBack);
        assertThat(customerBack.getWard()).isEqualTo(ward);

        ward.removeCustomer(customerBack);
        assertThat(ward.getCustomers()).doesNotContain(customerBack);
        assertThat(customerBack.getWard()).isNull();

        ward.customers(new HashSet<>(Set.of(customerBack)));
        assertThat(ward.getCustomers()).containsOnly(customerBack);
        assertThat(customerBack.getWard()).isEqualTo(ward);

        ward.setCustomers(new HashSet<>());
        assertThat(ward.getCustomers()).doesNotContain(customerBack);
        assertThat(customerBack.getWard()).isNull();
    }

    @Test
    void orderTest() {
        Ward ward = getWardRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        ward.addOrder(orderBack);
        assertThat(ward.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getWard()).isEqualTo(ward);

        ward.removeOrder(orderBack);
        assertThat(ward.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getWard()).isNull();

        ward.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(ward.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getWard()).isEqualTo(ward);

        ward.setOrders(new HashSet<>());
        assertThat(ward.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getWard()).isNull();
    }

    @Test
    void districtTest() {
        Ward ward = getWardRandomSampleGenerator();
        District districtBack = getDistrictRandomSampleGenerator();

        ward.setDistrict(districtBack);
        assertThat(ward.getDistrict()).isEqualTo(districtBack);

        ward.district(null);
        assertThat(ward.getDistrict()).isNull();
    }
}
