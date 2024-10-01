package com.project.domain;

import static com.project.domain.CustomerTestSamples.*;
import static com.project.domain.DistrictTestSamples.*;
import static com.project.domain.OrderTestSamples.*;
import static com.project.domain.ProvinceTestSamples.*;
import static com.project.domain.WardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.project.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DistrictTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(District.class);
        District district1 = getDistrictSample1();
        District district2 = new District();
        assertThat(district1).isNotEqualTo(district2);

        district2.setId(district1.getId());
        assertThat(district1).isEqualTo(district2);

        district2 = getDistrictSample2();
        assertThat(district1).isNotEqualTo(district2);
    }

    @Test
    void customerTest() {
        District district = getDistrictRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        district.addCustomer(customerBack);
        assertThat(district.getCustomers()).containsOnly(customerBack);
        assertThat(customerBack.getDistrict()).isEqualTo(district);

        district.removeCustomer(customerBack);
        assertThat(district.getCustomers()).doesNotContain(customerBack);
        assertThat(customerBack.getDistrict()).isNull();

        district.customers(new HashSet<>(Set.of(customerBack)));
        assertThat(district.getCustomers()).containsOnly(customerBack);
        assertThat(customerBack.getDistrict()).isEqualTo(district);

        district.setCustomers(new HashSet<>());
        assertThat(district.getCustomers()).doesNotContain(customerBack);
        assertThat(customerBack.getDistrict()).isNull();
    }

    @Test
    void orderTest() {
        District district = getDistrictRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        district.addOrder(orderBack);
        assertThat(district.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getDistrict()).isEqualTo(district);

        district.removeOrder(orderBack);
        assertThat(district.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getDistrict()).isNull();

        district.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(district.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getDistrict()).isEqualTo(district);

        district.setOrders(new HashSet<>());
        assertThat(district.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getDistrict()).isNull();
    }

    @Test
    void wardTest() {
        District district = getDistrictRandomSampleGenerator();
        Ward wardBack = getWardRandomSampleGenerator();

        district.addWard(wardBack);
        assertThat(district.getWards()).containsOnly(wardBack);
        assertThat(wardBack.getDistrict()).isEqualTo(district);

        district.removeWard(wardBack);
        assertThat(district.getWards()).doesNotContain(wardBack);
        assertThat(wardBack.getDistrict()).isNull();

        district.wards(new HashSet<>(Set.of(wardBack)));
        assertThat(district.getWards()).containsOnly(wardBack);
        assertThat(wardBack.getDistrict()).isEqualTo(district);

        district.setWards(new HashSet<>());
        assertThat(district.getWards()).doesNotContain(wardBack);
        assertThat(wardBack.getDistrict()).isNull();
    }

    @Test
    void provinceTest() {
        District district = getDistrictRandomSampleGenerator();
        Province provinceBack = getProvinceRandomSampleGenerator();

        district.setProvince(provinceBack);
        assertThat(district.getProvince()).isEqualTo(provinceBack);

        district.province(null);
        assertThat(district.getProvince()).isNull();
    }
}
