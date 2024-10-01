package com.project.domain;

import static com.project.domain.CustomerTestSamples.*;
import static com.project.domain.DistrictTestSamples.*;
import static com.project.domain.OrderTestSamples.*;
import static com.project.domain.ProvinceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.project.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProvinceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Province.class);
        Province province1 = getProvinceSample1();
        Province province2 = new Province();
        assertThat(province1).isNotEqualTo(province2);

        province2.setId(province1.getId());
        assertThat(province1).isEqualTo(province2);

        province2 = getProvinceSample2();
        assertThat(province1).isNotEqualTo(province2);
    }

    @Test
    void customerTest() {
        Province province = getProvinceRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        province.addCustomer(customerBack);
        assertThat(province.getCustomers()).containsOnly(customerBack);
        assertThat(customerBack.getProvince()).isEqualTo(province);

        province.removeCustomer(customerBack);
        assertThat(province.getCustomers()).doesNotContain(customerBack);
        assertThat(customerBack.getProvince()).isNull();

        province.customers(new HashSet<>(Set.of(customerBack)));
        assertThat(province.getCustomers()).containsOnly(customerBack);
        assertThat(customerBack.getProvince()).isEqualTo(province);

        province.setCustomers(new HashSet<>());
        assertThat(province.getCustomers()).doesNotContain(customerBack);
        assertThat(customerBack.getProvince()).isNull();
    }

    @Test
    void orderTest() {
        Province province = getProvinceRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        province.addOrder(orderBack);
        assertThat(province.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getProvince()).isEqualTo(province);

        province.removeOrder(orderBack);
        assertThat(province.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getProvince()).isNull();

        province.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(province.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getProvince()).isEqualTo(province);

        province.setOrders(new HashSet<>());
        assertThat(province.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getProvince()).isNull();
    }

    @Test
    void districtTest() {
        Province province = getProvinceRandomSampleGenerator();
        District districtBack = getDistrictRandomSampleGenerator();

        province.addDistrict(districtBack);
        assertThat(province.getDistricts()).containsOnly(districtBack);
        assertThat(districtBack.getProvince()).isEqualTo(province);

        province.removeDistrict(districtBack);
        assertThat(province.getDistricts()).doesNotContain(districtBack);
        assertThat(districtBack.getProvince()).isNull();

        province.districts(new HashSet<>(Set.of(districtBack)));
        assertThat(province.getDistricts()).containsOnly(districtBack);
        assertThat(districtBack.getProvince()).isEqualTo(province);

        province.setDistricts(new HashSet<>());
        assertThat(province.getDistricts()).doesNotContain(districtBack);
        assertThat(districtBack.getProvince()).isNull();
    }
}
